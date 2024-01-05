package com.enigma.x_food.feature.order_item.dto.request;

import com.enigma.x_food.feature.order.dto.request.OrderSubVarietyRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemRequest {
    @NotBlank(message = "Item ID cannot be empty")
    private String itemID;
    private List<OrderSubVarietyRequest> subVarieties;
    @NotNull(message = "Quantity cannot be empty")
    private Integer quantity;
}
