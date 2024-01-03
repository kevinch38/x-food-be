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
public class AdminTokenRequest {
    @NotBlank(message = "Token is required")
    private String token;
}
