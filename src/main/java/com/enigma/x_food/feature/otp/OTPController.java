package com.enigma.x_food.feature.otp;

import com.enigma.x_food.feature.otp.dto.request.NewOTPRequest;
import com.enigma.x_food.feature.otp.dto.response.OTPResponse;
import com.enigma.x_food.shared.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/otp")
@RequiredArgsConstructor
public class OTPController {
    private final OTPService pinService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNewOTP(@RequestBody NewOTPRequest request) {
        OTPResponse otpResponse = pinService.createNew(request);
        CommonResponse<OTPResponse> response = CommonResponse.<OTPResponse>builder()
                .message("successfully create new otp")
                .statusCode(HttpStatus.CREATED.value())
                .data(otpResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
