package com.enigma.x_food.feature.loyalty_point.dto.request;

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
public class UpdateLoyaltyPointRequest {
    @NotBlank(message = "Loyalty point ID cannot be empty")
    private String loyaltyPointID;
    @NotNull(message = "Loyalty point amount cannot be empty")
    private Double loyaltyPointAmount;
    @NotNull(message = "Expired at cannot be empty")
    private Timestamp expiredAt;
}
