package com.enigma.x_food.feature.order_item.dto.request;

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
public class OrderItemRequest {
    @NotBlank(message = "Item ID cannot be empty")
    private String itemID;
    @NotNull(message = "Quantity cannot be empty")
    private Integer quantity;
}
