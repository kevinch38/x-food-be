package com.enigma.x_food.feature.top_up.dto.request;

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
public class TopUpRequest {
    @NotNull(message = "Top up amount cannot be empty")
    private Double topUpAmount;
    @NotBlank(message = "Method cannot be empty")
    private String method;
    @NotNull(message = "Top up fee = cannot be empty")
    private Double topUpFee;
    @NotBlank(message = "Top up status ID cannot be empty")
    private String topUpStatusID;
    @NotBlank(message = "Balance ID cannot be empty")
    private String balanceID;
    @NotBlank(message = "Account ID cannot be empty")
    private String accountID;
}
