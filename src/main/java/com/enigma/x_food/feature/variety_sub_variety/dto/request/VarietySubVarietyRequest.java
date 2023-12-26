package com.enigma.x_food.feature.variety_sub_variety.dto.request;

import com.enigma.x_food.feature.sub_variety.SubVariety;
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
    @NotBlank(message = "Variety id cannot be empty")
    private String varietyID;
    @NotNull
    private SubVariety subVariety;
}
