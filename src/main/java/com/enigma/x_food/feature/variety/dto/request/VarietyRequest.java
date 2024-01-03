package com.enigma.x_food.feature.variety.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VarietyRequest {
    @NotBlank(message = "Variety name cannot be empty")
    private String varietyName;
    @NotNull(message = "Is required cannot be empty")
    private Boolean isRequired;
    @NotBlank(message = "Item ID cannot be empty")
    private String itemID;
}
