package com.enigma.x_food.feature.order.dto.request;

import com.enigma.x_food.feature.order_item.dto.request.OrderItemRequest;
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
public class OrderRequest {
    @NotBlank(message = "Account ID cannot be empty")
    private String accountID;
    @NotNull(message = "Order value cannot be empty")
    private Double orderValue;
    @NotBlank(message = "Notes cannot be empty")
    private String notes;
    @NotNull(message = "Table number cannot be empty")
    private Integer tableNumber;
    @NotBlank(message = "Branch ID cannot be empty")
    private String branchID;
    @NotNull(message = "Order item cannot be empty")
    private List<OrderItemRequest> orderItems;
}
