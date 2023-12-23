package com.enigma.x_food.feature.item_variety.dto.response;

import com.enigma.x_food.feature.variety.Variety;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemVarietyResponse {
    private String itemVarietyID;
    private Variety variety;
}
