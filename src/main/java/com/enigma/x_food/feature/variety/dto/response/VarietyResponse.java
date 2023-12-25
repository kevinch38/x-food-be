package com.enigma.x_food.feature.variety.dto.response;

import com.enigma.x_food.feature.user.variety_sub_variety.dto.response.VarietySubVarietyResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VarietyResponse {
    private String varietyID;
    private String varietyName;
    private Boolean isRequired;
    private Boolean isMultiSelect;
    private List<VarietySubVarietyResponse> varietySubVarieties;
}
