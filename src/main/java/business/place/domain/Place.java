package business.place.domain;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public record Place(
        Double latitude,
        Double longitude,
        Integer stayDurationMinutes,
        LocalTime openAt,
        LocalTime closeAt,
        LocalTime breakStartAt,
        LocalTime breakEndAt,
        List<DayOfWeek> closedDayOfWeeks
) {

    public static Place coordinateOnly(Double latitude, Double longitude) {
        return new Place(
                latitude,
                longitude,
                0,
                null,
                null,
                null,
                null,
                null
        );
    }
}
