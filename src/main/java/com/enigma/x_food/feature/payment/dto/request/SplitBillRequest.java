package com.enigma.x_food.feature.payment.dto.request;

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
public class SplitBillRequest {
    @NotBlank(message = "Account ID cannot be empty")
    private String accountID;
    @NotNull(message = "Payment amount cannot be empty")
    private Double paymentAmount;
    @NotBlank(message = "Friend Account ID cannot be empty")
    private String friendAccountID;
    @NotBlank(message = "Order ID cannot be empty")
    private String orderID;
    @NotNull(message = "Order items cannot be empty")
    private List<String> orderItems;
}
