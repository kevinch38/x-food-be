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
public class NewPromotionRequest {
    @NotBlank(message = "Merchant ID cannot be empty")
    private String merchantID;
    @NotNull(message = "Cost cannot be empty")
    private Double cost;
    @NotNull(message = "Max redeem ID cannot be empty")
    private Integer maxRedeem;
    @NotBlank(message = "Promotion value cannot be empty")
    private String promotionValue;
    @NotBlank(message = "Promotion description cannot be empty")
    private String promotionDescription;
    @NotBlank(message = "Promotion name cannot be empty")
    private String promotionName;
    @NotNull(message = "Quantity cannot be empty")
    private Integer quantity;
    @NotNull(message = "Expired date cannot be empty")
    private Timestamp expiredDate;
    @NotBlank(message = "Promotion status ID cannot be empty")
    private String promotionStatusID;
    @NotNull(message = "Created at cannot be empty")
    private Timestamp createdAt;
    @NotNull(message = "Updated at cannot be empty")
    private Timestamp updatedAt;
    private String notes;
}
