package com.enigma.x_food.feature.order.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateOrderRequest {
    @NotBlank(message = "Account ID cannot be empty")
    private String accountID;
    @NotBlank(message = "Order ID cannot be empty")
    private String orderID;
}
