package business.routie.service;

import api.validation.RoutieValidationRequest;
import api.validation.RoutieValidationResponse;
import business.routie.domain.RoutiePlace;
import business.routie.domain.route.MovingStrategy;
import business.routie.domain.route.RouteCalculationContext;
import business.routie.domain.route.RouteCalculator;
import business.routie.domain.route.Routes;
import business.routie.domain.routievalidator.RoutieValidator;
import business.routie.domain.routievalidator.ValidationContext;
import business.routie.domain.routievalidator.ValidationResult;
import business.routie.domain.routievalidator.ValidationStrategy;
import business.routie.domain.timeperiod.TimePeriodCalculator;
import business.routie.domain.timeperiod.TimePeriods;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoutieService {

    private static final int MIN_ROUTIE_PLACES_FOR_ROUTE = 2;

    private final TimePeriodCalculator timePeriodCalculator;
    private final RouteCalculator routeCalculator;
    private final RoutieValidator routieValidator;

    public RoutieValidationResponse validateRoutie(final RoutieValidationRequest routieValidationRequest) {
        List<RoutiePlace> routiePlaces = routieValidationRequest.placeRequests().stream()
                .map(RoutieValidationRequest.PlaceRequest::toRoutiePlace)
                .toList();

        Routes routes = getRoutes(
                routieValidationRequest.startDateTime(),
                routiePlaces,
                MovingStrategy.fromString(routieValidationRequest.movingStrategy())
        );

        TimePeriods timePeriods = timePeriodCalculator.calculateTimePeriods(
                routieValidationRequest.startDateTime(),
                routes,
                routiePlaces
        );

        ValidationContext validationContext = new ValidationContext(
                routieValidationRequest.startDateTime(),
                routieValidationRequest.endDateTime(),
                timePeriods
        );
        List<ValidationResult> validationResults = new ArrayList<>();

        for (final ValidationStrategy validationStrategy : ValidationStrategy.values()) {
            ValidationResult validationResult = routieValidator.validate(validationContext, validationStrategy);
            validationResults.add(validationResult);
        }

        return RoutieValidationResponse.from(validationResults);
    }

    private Routes getRoutes(
            final LocalDateTime startDateTime,
            final List<RoutiePlace> routiePlaces,
            final MovingStrategy movingStrategy
    ) {
        Routes routes = Routes.empty();
        RouteCalculationContext routeCalculationContext = new RouteCalculationContext(
                startDateTime,
                routiePlaces,
                movingStrategy
        );

        if (routiePlaces.size() >= MIN_ROUTIE_PLACES_FOR_ROUTE && movingStrategy != null) {
            routes = routeCalculator.calculateRoutes(routeCalculationContext);
        }
        return routes;
    }
}
