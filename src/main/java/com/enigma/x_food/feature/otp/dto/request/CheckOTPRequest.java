package com.enigma.x_food.feature.otp.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckOTPRequest {
    @NotBlank(message = "OTP id cannot be empty")
    private String otpID;
    @NotBlank(message = "Entered OTP cannot be empty")
    private String enteredOtp;
}