package com.enigma.x_food.feature.merchant_branch.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateMerchantBranchRequest {
    private String branchID;
    private String branchName;
    private String address;
    private String timezone;
    private String branchWorkingHoursID;
    private String cityID;
    private MultipartFile image;
    private String picName;
    private String picNumber;
    private String picEmail;
}
