package com.enigma.x_food.feature.voucher.dto.request;

import com.enigma.x_food.feature.promotion.Promotion;
import com.enigma.x_food.feature.user.User;
import com.enigma.x_food.feature.voucher_status.VoucherStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewVoucherRequest {
    @NotBlank(message = "Promotion ID is required")
    private String promotionID;

    @NotBlank(message = "Account ID is required")
    private String accountID;

    @NotNull(message = "Voucher value is required")
    private Integer voucherValue;

    @NotBlank(message = "Voucher code is required")
    private String voucherCode;
}
