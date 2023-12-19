package com.enigma.x_food.feature.pin;

import com.enigma.x_food.feature.pin.dto.request.CheckPinRequest;
import com.enigma.x_food.feature.pin.dto.request.UpdatePinRequest;
import com.enigma.x_food.shared.CommonResponse;
import com.enigma.x_food.feature.pin.dto.response.PinResponse;
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

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNewPin(@RequestBody UpdatePinRequest request) {
        PinResponse pinResponse = pinService.update(request);
        CommonResponse<PinResponse> response = CommonResponse.<PinResponse>builder()
                .message("successfully create new pin")
                .statusCode(HttpStatus.CREATED.value())
                .data(pinResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPinById(@PathVariable String id) {
        PinResponse pinResponse = pinService.getById(id);
        CommonResponse<PinResponse> response = CommonResponse.<PinResponse>builder()
                .message("successfully get pin")
                .statusCode(HttpStatus.OK.value())
                .data(pinResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> checkPin(@RequestBody CheckPinRequest request) {
        boolean pinResponse = pinService.checkPin(request);
        CommonResponse<Boolean> response = CommonResponse.<Boolean>builder()
                .message("successfully check pin")
                .statusCode(HttpStatus.OK.value())
                .data(pinResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
