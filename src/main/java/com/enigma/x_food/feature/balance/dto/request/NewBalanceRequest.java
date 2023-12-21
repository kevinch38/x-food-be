package com.enigma.x_food.feature.balance.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewBalanceRequest {
    @NotBlank(message = "Account ID cannot be empty")
    private String accountID;
    @NotNull(message = "Total balance cannot be empty")
    private Double totalBalance;
}
