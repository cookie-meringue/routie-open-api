package business.routie.domain.routievalidator;

import business.routie.domain.timeperiod.TimePeriods;

import java.time.LocalDateTime;

public record ValidationContext(
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        TimePeriods timePeriods
) {
}
