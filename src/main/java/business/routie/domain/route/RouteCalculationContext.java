package business.routie.domain.route;

import business.routie.domain.RoutiePlace;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class RouteCalculationContext {

    @Getter
    private final List<RoutiePlace> routiePlaces;
    @Getter
    private final MovingStrategy movingStrategy;
    private final LocalDateTime startDateTime;

    public RouteCalculationContext(
            final LocalDateTime startDateTime,
            final List<RoutiePlace> routiePlaces,
            final MovingStrategy movingStrategy
    ) {
        this.routiePlaces = routiePlaces;
        this.movingStrategy = movingStrategy;
        this.startDateTime = startDateTime;
    }

    public Optional<LocalDateTime> getStartDateTime() {
        return Optional.ofNullable(startDateTime);
    }
}
