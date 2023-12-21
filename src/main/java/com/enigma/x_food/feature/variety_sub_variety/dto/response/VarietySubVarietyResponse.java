package com.enigma.x_food.feature.variety_sub_variety.dto.response;

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
public class VarietySubVarietyResponse {
    private String varietySubVarietyID;
    private String subVarietyID;
    private String varietyID;
}
