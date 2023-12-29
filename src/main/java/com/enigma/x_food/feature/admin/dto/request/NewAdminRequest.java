package com.enigma.x_food.feature.admin.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewAdminRequest {
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;
}
