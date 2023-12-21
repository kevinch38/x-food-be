package com.enigma.x_food.feature.merchant_branch.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchMerchantBranchRequest {
    private Integer page;
    private Integer size;
    private String direction;
    private String sortBy;
    private String branchID;
    private String merchantID;
    private String branchName;
    private String address;
    private String timezone;
    private String branchWorkingHoursID;
    private String cityID;
    private String picName;
    private String picNumber;
    private String picEmail;
}
