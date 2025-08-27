package business.routie.infrastructure.routievalidator;

import business.routie.domain.routievalidator.RoutieValidator;
import business.routie.domain.routievalidator.ValidationContext;
import business.routie.domain.routievalidator.ValidationResult;
import business.routie.domain.routievalidator.ValidationStrategy;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class RoutieValidatorComposite implements RoutieValidator {

    private final List<RoutieValidator> routeValidators;

    @Override
    public boolean supportsStrategy(final ValidationStrategy validationStrategy) {
        return validationStrategy != null;
    }

    @Override
    public ValidationResult validate(
            final ValidationContext validationContext,
            final ValidationStrategy validationStrategy
    ) {
        return selectValidityCalculator(validationStrategy).validate(
                validationContext,
                validationStrategy
        );
    }

    private RoutieValidator selectValidityCalculator(final ValidationStrategy validationStrategy) {
        return routeValidators.stream()
                .filter(calculator -> calculator.supportsStrategy(validationStrategy))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("루티 유효성 검사 전략을 지원하는 검증기가 없습니다."));
    }
}
