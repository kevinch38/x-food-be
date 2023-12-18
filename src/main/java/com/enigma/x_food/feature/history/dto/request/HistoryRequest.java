package com.enigma.x_food.feature.history.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistoryRequest {
    @NotBlank(message = "Transaction type cannot be empty")
    private String transactionType;
    @NotNull(message = "History value cannot be empty")
    private Double historyValue;
    @NotNull(message = "Transaction date cannot be empty")
    private LocalDate transactionDate;
    @NotNull(message = "Credit cannot be empty")
    private Boolean credit;
    @NotNull(message = "Debit name cannot be empty")
    private Boolean debit;
    private String orderID;
    private Timestamp paymentID;
    private String topUpID;
    @NotBlank(message = "Account ID cannot be empty")
    private String accountID;
}
