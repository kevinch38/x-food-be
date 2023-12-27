package com.enigma.x_food.feature.voucher.dto.response;

import com.enigma.x_food.feature.promotion.Promotion;
import com.enigma.x_food.feature.user.User;
import com.enigma.x_food.feature.voucher_status.VoucherStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoucherResponse {
    private String voucherID;

    private String promotionID;

    private String userID;

    private Integer voucherValue;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Timestamp expiredDate;

    private String voucherCode;

    private String voucherStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Timestamp createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Timestamp updatedAt;
}
