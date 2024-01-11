package com.enigma.x_food.feature.promotion.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApprovalPromotionRequest {
    @NotBlank(message = "Promotion ID cannot be empty")
    private String promotionID;
    private String notes;
}
