package api.validation;

import business.routie.service.RoutieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ValidationController {

    private final RoutieService routieService;

    @PostMapping("/v1/validity")
    public ResponseEntity<RoutieValidationResponse> validateRoutie(
            @Valid @RequestBody RoutieValidationRequest routieValidationRequest
    ) {
        return ResponseEntity.ok(routieService.validateRoutie(routieValidationRequest));
    }
}
