package com.enigma.x_food.feature.sub_variety.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubVarietyResponse {
    private String subVarietyID;
    private String branchID;
    private String subVarName;
    private Integer subVarStock;
}
