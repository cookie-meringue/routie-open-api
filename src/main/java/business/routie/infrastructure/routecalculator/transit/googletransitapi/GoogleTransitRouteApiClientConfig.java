package business.routie.infrastructure.routecalculator.transit.googletransitapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.net.URI;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
@RequiredArgsConstructor
public class GoogleTransitRouteApiClientConfig {

    private final ObjectMapper objectMapper;
    @Value("${google.api.key}")
    private String googleApiKey;

    @Bean
    public GoogleTransitRouteApiClient googleTransitApiClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl("https://routes.googleapis.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .defaultHeader("X-Goog-Api-Key", googleApiKey)
                .defaultHeader("X-Goog-FieldMask", "routes.distanceMeters,routes.duration")
                .defaultStatusHandler(new GoogleTransitApiResponseErrorHandler(objectMapper))
                .build();

        return new GoogleTransitRouteApiClient(restClient);
    }

    @Slf4j
    private record GoogleTransitApiResponseErrorHandler(ObjectMapper objectMapper) implements ResponseErrorHandler {

            @Override
            public boolean hasError(final ClientHttpResponse response) {
                try {
                    return response.getStatusCode().isError();
                } catch (final IOException ioException) {
                    log.error("Google Routes API 응답 상태 코드 확인 중 I/O 오류 발생", ioException);
                    throw new IllegalStateException("Google Routes API 응답을 읽는 도중 오류가 발생했습니다.");
                }
            }

            @Override
            public void handleError(
                    final URI url,
                    final HttpMethod method,
                    final ClientHttpResponse response
            ) throws IOException {
                final GoogleErrorResponse errorResponse = objectMapper.readValue(
                        response.getBody(),
                        GoogleErrorResponse.class
                );
                final GoogleErrorPayload errorPayload = errorResponse.error();

                log.warn(
                        "Google Routes API 오류 발생. HTTP Status: {}, Code: {}, Message: {}",
                        response.getStatusCode().value(),
                        errorPayload.status(),
                        errorPayload.message()
                );

                throw new IllegalStateException("Google Routes API 요청이 실패했습니다.");
            }

            public record GoogleErrorResponse(
                    @JsonProperty("error") GoogleErrorPayload error
            ) {
            }

            public record GoogleErrorPayload(
                    @JsonProperty("code") int httpStatusCode,
                    @JsonProperty("message") String message,
                    @JsonProperty("status") String status
            ) {
            }
        }
}
