package com.enigma.x_food.feature.merchant_branch.dto.request;

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
public class UpdateMerchantBranchRequest {
    @NotBlank(message = "Merchant branch ID cannot be empty")
    private String merchantBranchID;
    @NotBlank(message = "Merchant ID cannot be empty")
    private String merchantID;
    @NotBlank(message = "Branch name cannot be empty")
    private String branchName;
    @NotBlank(message = "Address cannot be empty")
    private String address;
    @NotBlank(message = "Timezone cannot be empty")
    private String timezone;
    @NotBlank(message = "Branch working hours ID cannot be empty")
    private String branchWorkingHoursID;
    @NotBlank(message = "City ID cannot be empty")
    private String cityID;
    @NotNull(message = "Updated at cannot be empty")
    private Timestamp updatedAt;
}
