package business.routie.infrastructure.routecalculator.driving.kakaodrivingapi;

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
public class KakaoDrivingRouteApiClientConfig {

    private final ObjectMapper objectMapper;
    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    @Bean
    public KakaoDrivingRouteApiClient kakaoDrivingApiClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl("https://apis-navi.kakaomobility.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoApiKey)
                .defaultStatusHandler(new KakaoDrivingApiResponseErrorHandler(objectMapper))
                .build();

        return new KakaoDrivingRouteApiClient(restClient);
    }

    @Slf4j
    private record KakaoDrivingApiResponseErrorHandler(ObjectMapper objectMapper) implements ResponseErrorHandler {

            @Override
            public boolean hasError(final ClientHttpResponse response) {
                try {
                    return response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError();
                } catch (final IOException ioException) {
                    throw new IllegalStateException("Kakao 길찾기 API 응답을 읽는 도중 오류가 발생했습니다.", ioException);
                }
            }

            @Override
            public void handleError(
                    final URI url,
                    final HttpMethod method,
                    final ClientHttpResponse response
            ) throws IOException {
                log.warn(
                        "Kakao 길찾기 API 오류 발생: {}",
                        objectMapper.readValue(response.getBody(), KakaoDrivingApiErrorResponse.class)
                );

                throw new IllegalStateException("Kakao 길찾기 API 요청이 실패했습니다.");
            }

            private record KakaoDrivingApiErrorResponse(
                    @JsonProperty("code") String code,
                    @JsonProperty("msg") String message
            ) {
            }
        }
}
