package com.enigma.x_food.feature.pin.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckPinRequest {
    @NotBlank(message = "Pin ID cannot be empty")
    private String pinID;
    @NotBlank(message = "Pin cannot be empty")
    private String pin;
}
