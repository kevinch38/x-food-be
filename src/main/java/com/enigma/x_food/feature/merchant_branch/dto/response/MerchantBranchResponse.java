package com.enigma.x_food.feature.merchant_branch.dto.response;

import com.enigma.x_food.feature.branch_working_hours.dto.response.BranchWorkingHoursResponse;
import com.enigma.x_food.feature.city.dto.response.CityResponse;
import com.enigma.x_food.feature.item.dto.response.ItemResponse;
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
public class MerchantBranchResponse {
    private String branchID;
    private String merchantID;
    private String branchName;
    private String address;
    private String timezone;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Timestamp createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Timestamp updatedAt;
    private List<BranchWorkingHoursResponse> branchWorkingHours;
    private String status;
    private CityResponse city;
    private List<ItemResponse> items;
    private String adminID;
    private String picName;
    private String picNumber;
    private String picEmail;
    private byte[] image;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Timestamp joinDate;
}
