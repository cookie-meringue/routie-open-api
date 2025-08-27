package business.routie.domain.timeperiod;

import business.routie.domain.RoutiePlace;
import business.routie.domain.route.Route;
import business.routie.domain.route.Routes;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class TimePeriodCalculator {

    public TimePeriods calculateTimePeriods(
            final LocalDateTime startDateTime,
            final Routes routes,
            final List<RoutiePlace> routiePlaces
    ) {
        TimePeriods timePeriods = TimePeriods.empty();

        if (routiePlaces.size() == 1) {
            RoutiePlace firstRoutiePlace = routiePlaces.getFirst();

            return TimePeriods.empty().
                    withAdded(
                            firstRoutiePlace,
                            new TimePeriod(
                                    firstRoutiePlace,
                                    startDateTime,
                                    startDateTime.plusMinutes(firstRoutiePlace.place().stayDurationMinutes())
                            )
                    );
        }

        List<RoutiePlace> orderedRoutiePlaces = routes.orderedRoutiePlaces();
        LocalDateTime currentTime = startDateTime;

        for (final RoutiePlace routiePlace : orderedRoutiePlaces) {
            LocalDateTime start = currentTime;
            LocalDateTime end = start.plusMinutes(routiePlace.place().stayDurationMinutes());

            timePeriods = timePeriods.withAdded(routiePlace, new TimePeriod(routiePlace, start, end));

            Route route = routes.getByRoutiePlace(routiePlace);
            if (route != null) {
                currentTime = end.plusMinutes(route.duration());
            }
        }

        return timePeriods;
    }
}
