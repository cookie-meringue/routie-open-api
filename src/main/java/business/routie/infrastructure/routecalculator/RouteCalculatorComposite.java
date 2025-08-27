package business.routie.infrastructure.routecalculator;

import business.routie.domain.route.MovingStrategy;
import business.routie.domain.route.RouteCalculationContext;
import business.routie.domain.route.RouteCalculator;
import business.routie.domain.route.Routes;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class RouteCalculatorComposite implements RouteCalculator {

    private final List<RouteCalculator> routeCalculators;

    @Override
    public boolean supportsStrategy(final MovingStrategy movingStrategy) {
        return movingStrategy != null;
    }

    @Override
    public Routes calculateRoutes(final RouteCalculationContext routeCalculationContext) {
        return selectRouteCalculator(routeCalculationContext).calculateRoutes(routeCalculationContext);
    }

    private RouteCalculator selectRouteCalculator(final RouteCalculationContext routeCalculationContext) {
        MovingStrategy movingStrategy = routeCalculationContext.getMovingStrategy();
        return routeCalculators.stream()
                .filter(calculator -> calculator.supportsStrategy(movingStrategy))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("루티 계산 전략을 지원하는 계산기가 없습니다."));
    }
}
