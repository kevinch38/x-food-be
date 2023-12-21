package com.enigma.x_food.feature.variety_sub_variety.dto.request;

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
public class VarietySubVarietyRequest {
    @NotBlank(message = "Sub variety id cannot be empty")
    private String subVarietyID;
    @NotNull(message = "Variety id cannot be empty")
    private String varietyID;
}
