package business.routie.infrastructure.routievalidator;

import business.routie.domain.routievalidator.RoutieValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Configuration
public class RoutieValidatorConfig {

    @Bean
    @Primary
    public RoutieValidator routieValidator(final List<RoutieValidator> routieValidators) {
        return new RoutieValidatorComposite(routieValidators);
    }
}
