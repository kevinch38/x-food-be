package com.enigma.x_food.feature.balance.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateBalanceRequest {
    @NotBlank(message = "Balance ID cannot be empty")
    private String balanceID;
    @NotBlank(message = "Total balance cannot be empty")
    private Double totalBalance;
}
