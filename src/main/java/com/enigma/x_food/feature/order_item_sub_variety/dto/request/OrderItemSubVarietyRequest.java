package com.enigma.x_food.feature.order_item_sub_variety.dto.request;

import com.enigma.x_food.feature.order_item.OrderItem;
import com.enigma.x_food.feature.sub_variety.SubVariety;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemSubVarietyRequest {
    @NotBlank(message = "Variety id cannot be empty")
    private OrderItem orderItem;
    @NotBlank
    private SubVariety subVariety;
}
