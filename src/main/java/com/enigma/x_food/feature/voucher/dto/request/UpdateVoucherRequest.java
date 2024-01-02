package com.enigma.x_food.feature.voucher.dto.request;

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
public class UpdateVoucherRequest {
    @NotBlank(message = "ID is required")
    private String voucherID;

    @NotNull(message = "Voucher value is required")
    private Integer voucherValue;

    @NotBlank(message = "Voucher code is required")
    private String voucherCode;
}
