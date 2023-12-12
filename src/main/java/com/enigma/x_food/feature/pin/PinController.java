package com.enigma.x_food.feature.pin;

import com.enigma.x_food.feature.pin.dto.request.NewPinRequest;
import com.enigma.x_food.shared.CommonResponse;
import com.enigma.x_food.feature.pin.dto.response.PinResponse;
import com.enigma.x_food.feature.pin.PinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pins")
@RequiredArgsConstructor
public class PinController {
    private final PinService pinService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNewPin(@RequestBody NewPinRequest request) {
        PinResponse pinResponse = pinService.createNew(request);
        CommonResponse<PinResponse> response = CommonResponse.<PinResponse>builder()
                .message("successfully create new pin")
                .statusCode(HttpStatus.CREATED.value())
                .data(pinResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
