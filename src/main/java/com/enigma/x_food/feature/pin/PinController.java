package com.enigma.x_food.feature.pin;

import com.enigma.x_food.feature.pin.dto.request.CheckPinRequest;
import com.enigma.x_food.feature.pin.dto.request.UpdatePinRequest;
import com.enigma.x_food.shared.CommonResponse;
import com.enigma.x_food.feature.pin.dto.response.PinResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pins")
@RequiredArgsConstructor
@PreAuthorize("permitAll")
@SecurityRequirement(name = "Bearer Authentication")
public class PinController {
    private final PinService pinService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@PathVariable String id) {
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
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@RequestBody UpdatePinRequest request) {
        PinResponse pinResponse = pinService.update(request);
        CommonResponse<PinResponse> response = CommonResponse.<PinResponse>builder()
                .message("successfully update pin")
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
