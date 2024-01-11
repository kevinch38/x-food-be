package com.enigma.x_food.feature.merchant.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApprovalMerchantRequest {
    @NotBlank(message = "Merchant ID cannot be empty")
    private String merchantID;
    private String notes;
}
