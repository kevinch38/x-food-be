package com.enigma.x_food.feature.otp.dto.order_item.dto.request;

import com.enigma.x_food.feature.order.dto.request.OrderSubVarietyRequest;
import com.enigma.x_food.feature.sub_variety.SubVariety;
import com.enigma.x_food.feature.sub_variety.dto.request.SubVarietyRequest;
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
    @NotNull(message = "Sub varieties cannot be empty")
    private List<OrderSubVarietyRequest> subVarieties;
    @NotNull(message = "Quantity cannot be empty")
    private Integer quantity;
}
