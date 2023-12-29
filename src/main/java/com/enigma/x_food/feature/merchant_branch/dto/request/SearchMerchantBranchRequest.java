package com.enigma.x_food.feature.merchant_branch.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchMerchantBranchRequest {
    private Integer page;
    private Integer size;
    private String direction;
    private String sortBy;
    private String merchantID;
    private String branchID;
    private String branchName;
    private String status;
    private String city;
    private LocalDate startJoinDate;
    private LocalDate endJoinDate;
}
