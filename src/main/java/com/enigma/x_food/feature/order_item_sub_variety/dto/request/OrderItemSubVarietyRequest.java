package com.enigma.x_food.feature.order_item_sub_variety.dto.request;

import com.enigma.x_food.feature.order_item.OrderItem;
import com.enigma.x_food.feature.sub_variety.SubVariety;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemSubVarietyRequest {
    @NotNull(message = "Variety ID cannot be empty")
    private OrderItem orderItem;
    @NotNull(message = "Sub variety ID cannot be empty")
    private SubVariety subVariety;
}
