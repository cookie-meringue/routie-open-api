package api.validation;

import business.routie.domain.RoutiePlace;
import business.routie.domain.routievalidator.ValidationResult;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record RoutieValidationResponse(
        @JsonProperty("validationResults") List<ValidationResultResponse> validationResultResponses
) {
    public static RoutieValidationResponse from(final List<ValidationResult> validationResults) {
        return new RoutieValidationResponse(
                validationResults.stream()
                        .map(ValidationResultResponse::from)
                        .toList()
        );
    }

    public record ValidationResultResponse(
            @JsonProperty("code") String validationCode,
            @JsonProperty("isValid") boolean isValid,
            @JsonProperty("invalidPlaceSequences") List<Integer> invalidPlaceSequences
    ) {
        public static ValidationResultResponse from(final ValidationResult validationResult) {
            return new ValidationResultResponse(
                    validationResult.strategy().getValidationCode(),
                    validationResult.isValid(),
                    validationResult.invalidRoutiePlaces().stream()
                            .map(RoutiePlace::sequence)
                            .toList()
            );
        }
    }
}
