package business.routie.infrastructure.routecalculator.driving;

import business.routie.domain.RoutiePlace;
import business.routie.domain.route.*;
import business.routie.infrastructure.routecalculator.driving.kakaodrivingapi.KakaoDrivingRouteApiClient;
import business.routie.infrastructure.routecalculator.driving.kakaodrivingapi.KakaoDrivingRouteApiRequest;
import business.routie.infrastructure.routecalculator.driving.kakaodrivingapi.KakaoDrivingRouteApiResponse;
import business.routie.infrastructure.routecalculator.driving.kakaodrivingapi.KakaoDrivingRouteApiResponse.SectionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DrivingRouteCalculator implements RouteCalculator {

    private final KakaoDrivingRouteApiClient kakaoDrivingRouteApiClient;

    @Override
    public boolean supportsStrategy(final MovingStrategy movingStrategy) {
        return movingStrategy.equals(MovingStrategy.DRIVING);
    }

    @Override
    public Routes calculateRoutes(final RouteCalculationContext routeCalculationContext) {
        List<RoutiePlace> routiePlaces = routeCalculationContext.getRoutiePlaces();
        KakaoDrivingRouteApiResponse kakaoDrivingRouteApiResponse = kakaoDrivingRouteApiClient.getRoute(
                KakaoDrivingRouteApiRequest.from(routiePlaces)
        );
        List<SectionResponse> sectionResponses = getRouteResponse(kakaoDrivingRouteApiResponse).sectionResponses();

        return mapToRoutes(routiePlaces, sectionResponses);
    }

    private KakaoDrivingRouteApiResponse.RouteResponse getRouteResponse(
            final KakaoDrivingRouteApiResponse kakaoDrivingRouteApiResponse
    ) {
        return kakaoDrivingRouteApiResponse.routeResponses().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("카카오 길찾기 API 응답에 경로 정보가 없습니다."));
    }

    private Routes mapToRoutes(
            final List<RoutiePlace> routiePlaces,
            final List<SectionResponse> sectionResponses
    ) {
        Map<RoutiePlace, Route> routeMap = new HashMap<>();
        for (int i = 0; i < sectionResponses.size(); i++) {
            SectionResponse sectionResponse = sectionResponses.get(i);
            RoutiePlace from = routiePlaces.get(i);
            RoutiePlace to = routiePlaces.get(i + 1);
            Route route = new Route(
                    from, to, sectionResponse.duration() / 60,
                    sectionResponse.distance()
            );
            routeMap.put(from, route);
        }
        return new Routes(routeMap);
    }
}
