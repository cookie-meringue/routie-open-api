package business.routie.infrastructure.routecalculator;

import business.routie.domain.route.RouteCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Configuration
public class RouteCalculatorConfig {

    @Bean
    @Primary
    public RouteCalculator routeCalculator(final List<RouteCalculator> routeCalculators) {
        return new RouteCalculatorComposite(routeCalculators);
    }
}
