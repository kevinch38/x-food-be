package com.enigma.x_food.feature.payment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompleteSplitBillRequest {
    @NotBlank(message = "Account ID cannot be empty")
    private String accountID;
    @NotBlank(message = "Payment ID cannot be empty")
    private String paymentID;
}
