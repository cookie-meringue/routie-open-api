package api.validation;

import business.place.domain.Place;
import business.routie.domain.RoutiePlace;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record RoutieValidationRequest(
        @JsonProperty("places") @NotNull List<PlaceRequest> placeRequests,
        @JsonProperty("startAt") LocalDateTime startDateTime,
        @JsonProperty("endAt") LocalDateTime endDateTime,
        @JsonProperty("movingStrategy") @NotNull String movingStrategy
) {

    public record PlaceRequest(
            @JsonProperty("sequence") @NotNull Integer sequence,
            @JsonProperty("latitude") @NotNull Double latitude,
            @JsonProperty("longitude") @NotNull Double longitude
    ) {

        public Place toPlace() {
            return Place.coordinateOnly(latitude, longitude);
        }

        public RoutiePlace toRoutiePlace() {
            return new RoutiePlace(sequence, toPlace());
        }
    }
}
