package com.enigma.x_food.feature.order_item_split.order.dto.request;

import com.enigma.x_food.feature.order_item.OrderItem;
import com.enigma.x_food.feature.order_item.dto.request.OrderItemRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewOrderItemSplitRequest {
    @NotNull(message = "Order item ID cannot be empty")
    private String orderItemID;
}
