package business.routie.domain.timeperiod;

import business.routie.domain.RoutiePlace;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TimePeriods {

    private static final Comparator<RoutiePlace> ROUTIE_PLACE_COMPARATOR =
            Comparator.comparing(RoutiePlace::sequence);

    private final TreeMap<RoutiePlace, TimePeriod> timePeriods;

    public TimePeriods(final Map<RoutiePlace, TimePeriod> timePeriods) {
        this.timePeriods = new TreeMap<>(ROUTIE_PLACE_COMPARATOR);
        this.timePeriods.putAll(timePeriods);
    }

    public static TimePeriods empty() {
        return new TimePeriods(new TreeMap<>(ROUTIE_PLACE_COMPARATOR));
    }

    public TimePeriods withAdded(final RoutiePlace routiePlace, final TimePeriod timePeriod) {
        Map<RoutiePlace, TimePeriod> newTimePeriods = new TreeMap<>(ROUTIE_PLACE_COMPARATOR);
        newTimePeriods.putAll(this.timePeriods);
        newTimePeriods.put(routiePlace, timePeriod);

        return new TimePeriods(newTimePeriods);
    }

    public TimePeriod getByRoutiePlace(final RoutiePlace place) {
        return timePeriods.get(place);
    }

    public void add(final RoutiePlace routiePlace, final TimePeriod timePeriod) {
        timePeriods.put(routiePlace, timePeriod);
    }

    public boolean contains(final RoutiePlace routiePlace) {
        return timePeriods.containsKey(routiePlace);
    }

    public int size() {
        return timePeriods.size();
    }

    public List<TimePeriod> orderedList() {
        return List.copyOf(timePeriods.values());
    }

    public List<RoutiePlace> routiePlaces() {
        return List.copyOf(timePeriods.keySet());
    }
}
