package com.enigma.x_food.feature.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminAuthRequest {
    @NotBlank(message = "Admin email is required")
    private String adminEmail;
    @NotBlank(message = "Password is required")
    private String password;
}
