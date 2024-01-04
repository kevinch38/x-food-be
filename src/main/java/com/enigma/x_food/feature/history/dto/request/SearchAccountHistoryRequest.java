package com.enigma.x_food.feature.history.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchAccountHistoryRequest {
    private String direction;
    private String sortBy;
    private String accountID;
}
