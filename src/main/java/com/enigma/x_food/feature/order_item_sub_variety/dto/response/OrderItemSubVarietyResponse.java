package com.enigma.x_food.feature.order_item_sub_variety.dto.response;

import com.enigma.x_food.feature.sub_variety.dto.response.SubVarietyResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemSubVarietyResponse {
    private String orderItemSubVarietyID;
    private SubVarietyResponse subVariety;
}
