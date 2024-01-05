package com.enigma.x_food.feature.otp.dto.order_item.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchOrderItemRequest {
    private String direction;
    private String sortBy;
    private String branchID;
}
