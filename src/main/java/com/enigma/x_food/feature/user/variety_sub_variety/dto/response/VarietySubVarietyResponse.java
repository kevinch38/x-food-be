package com.enigma.x_food.feature.user.variety_sub_variety.dto.response;

import com.enigma.x_food.feature.sub_variety.dto.response.SubVarietyResponse;
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
    private SubVarietyResponse subVariety;
}
