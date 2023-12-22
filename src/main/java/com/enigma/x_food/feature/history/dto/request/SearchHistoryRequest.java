package com.enigma.x_food.feature.history.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchHistoryRequest {
    private Integer page;
    private Integer size;
    private String direction;
    private String sortBy;
    private String historyID;
    private String transactionType;
    private Boolean credit;
    private Boolean debit;
    private String orderID;
    private String paymentID;
    private String topUpID;
    private String accountID;
}
