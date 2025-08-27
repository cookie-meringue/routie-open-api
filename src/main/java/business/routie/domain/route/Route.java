package business.routie.domain.route;

import business.routie.domain.RoutiePlace;

public record Route(
        RoutiePlace from,
        RoutiePlace to,
        int duration,
        int distance
) {
}
