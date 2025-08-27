package business.routie.domain.timeperiod;

import business.routie.domain.RoutiePlace;

import java.time.LocalDateTime;

public record TimePeriod(
        RoutiePlace routiePlace,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
}
