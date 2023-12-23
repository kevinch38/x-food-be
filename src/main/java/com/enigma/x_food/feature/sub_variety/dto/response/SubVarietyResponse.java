package com.enigma.x_food.feature.sub_variety.dto.response;

import com.enigma.x_food.feature.variety_sub_variety.VarietySubVariety;
import com.enigma.x_food.feature.variety_sub_variety.dto.request.VarietySubVarietyRequest;
import com.enigma.x_food.feature.variety_sub_variety.dto.response.VarietySubVarietyResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubVarietyResponse {
    private String subVarietyID;
    private String branchID;
    private String subVarName;
    private Integer subVarStock;
    private List<VarietySubVarietyResponse> varietySubVariety;
}
