package com.enigma.x_food.feature.merchant.dto.response;

import com.enigma.x_food.feature.merchant_branch.dto.response.MerchantBranchResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MerchantResponse {
    private String merchantID;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Timestamp joinDate;
    private String merchantName;
    private String picName;
    private String picNumber;
    private String picEmail;
    private String merchantDescription;
    private String adminId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Timestamp createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Timestamp updatedAt;
    private String status;
    private String notes;
    private List<MerchantBranchResponse> merchantBranches;
}
