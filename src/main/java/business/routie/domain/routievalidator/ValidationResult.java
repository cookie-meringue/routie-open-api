package business.routie.domain.routievalidator;

import business.routie.domain.RoutiePlace;

import java.util.List;

public record ValidationResult(
        boolean isValid,
        ValidationStrategy strategy,
        List<RoutiePlace> invalidRoutiePlaces
) {

    public static ValidationResult withoutRoutiePlaces(final boolean isValid, final ValidationStrategy strategy) {
        return new ValidationResult(isValid, strategy, List.of());
    }
}
