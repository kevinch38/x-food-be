package com.enigma.x_food.feature.otp;

import com.enigma.x_food.feature.otp.dto.request.CheckOTPRequest;
import com.enigma.x_food.feature.otp.dto.response.OTPTokenResponse;
import com.enigma.x_food.shared.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otp")
@RequiredArgsConstructor
public class OTPController {
    private final OTPService otpService;
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> checkOtp(@RequestBody CheckOTPRequest request) {
        OTPTokenResponse isTrue = otpService.checkOtp(request);
        CommonResponse<OTPTokenResponse> response = CommonResponse.<OTPTokenResponse>builder()
                .message("successfully check otp")
                .statusCode(HttpStatus.OK.value())
                .data(isTrue)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
