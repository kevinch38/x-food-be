package com.enigma.x_food.feature.history.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchHistoryRequest {
    private Integer page;
    private Integer size;
    private String direction;
    private String sortBy;
    private String accountID;
    private String transactionType;
    private LocalDate startTransactionDate;
    private LocalDate endTransactionDate;
}
