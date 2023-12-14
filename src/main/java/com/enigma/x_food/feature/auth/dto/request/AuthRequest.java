package com.enigma.x_food.feature.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequest {
    @NotBlank(message = "username is required")
    @Pattern(regexp = "^[0-9]+$", message = "invalid phone number")
    @Size(max = 15, message = "must be less than 16 character")
    private String phoneNumber;
}
