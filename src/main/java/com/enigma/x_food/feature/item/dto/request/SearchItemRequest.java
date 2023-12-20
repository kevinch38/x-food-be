package com.enigma.x_food.feature.item.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchItemRequest {
    private String direction;
    private String sortBy;
    private String branchID;
}
