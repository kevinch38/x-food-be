package com.enigma.x_food.feature.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private String orderID;
    private String accountID;
    private String historyID;
    private Double orderValue;
    private String notes;
    private Integer tableNumber;
    private String orderStatus;
    private String branchID;
}
