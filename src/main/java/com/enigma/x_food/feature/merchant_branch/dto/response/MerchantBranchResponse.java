package com.enigma.x_food.feature.merchant_branch.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MerchantBranchResponse {
    private String branchID;
    private String merchantID;
    private String branchName;
    private String address;
    private String timezone;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String branchWorkingHoursID;
    private String cityID;
}
