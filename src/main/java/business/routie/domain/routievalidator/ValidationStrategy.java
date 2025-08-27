package business.routie.domain.routievalidator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ValidationStrategy {

    IS_WITHIN_TOTAL_TIME("IS_WITHIN_TOTAL_TIME");

    private final String validationCode;
}
