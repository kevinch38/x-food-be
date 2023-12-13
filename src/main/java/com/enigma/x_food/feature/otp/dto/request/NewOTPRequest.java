package com.enigma.x_food.feature.otp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewOTPRequest {
    @NotBlank(message = "Pin cannot be empty")
    private String otp;
    @NotBlank(message = "Account ID cannot be empty")
    private String accountID;
}
