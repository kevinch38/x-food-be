package com.enigma.x_food.feature.voucher.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchVoucherPromotionRequest {
    @NotBlank(message = "Promotion ID is required")
    private String promotionID;

    @NotBlank(message = "Account ID is required")
    private String accountID;
}
