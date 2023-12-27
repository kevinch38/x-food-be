package com.enigma.x_food.feature.promotion.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatePromotionRequest {
    @NotBlank(message = "Promotion ID cannot be empty")
    private String promotionID;
    @NotBlank(message = "Merchant ID cannot be empty")
    private String merchantID;
    @NotNull(message = "Cost cannot be empty")
    private Integer cost;
    @NotNull(message = "Max redeem ID cannot be empty")
    private Integer maxRedeem;
    @NotNull(message = "Promotion value cannot be empty")
    private Integer promotionValue;
    @NotBlank(message = "Promotion description cannot be empty")
    private String promotionDescription;
    @NotBlank(message = "Promotion name cannot be empty")
    private String promotionName;
    @NotNull(message = "Quantity cannot be empty")
    private Integer quantity;
    @NotNull(message = "Expired date cannot be empty")
    private Timestamp expiredDate;
    private String notes;
}
