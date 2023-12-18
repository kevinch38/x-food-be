package com.enigma.x_food.feature.history.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistoryResponse {
    private String historyID;
    private String transactionType;
    private Double historyValue;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate transactionDate;
    private Boolean credit;
    private Boolean debit;
    private String orderID;
    private String paymentID;
    private String topUpID;
    private String accountID;
}
