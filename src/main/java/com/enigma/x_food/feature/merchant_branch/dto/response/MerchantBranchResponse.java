package com.enigma.x_food.feature.merchant_branch.dto.response;

import com.enigma.x_food.feature.city.dto.response.CityResponse;
import com.enigma.x_food.feature.item.Item;
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
    private String branchWorkingHoursID;
    private String status;
    private CityResponse city;
    private List<Item> itemList;
    private String picName;
    private String picNumber;
    private String picEmail;
    private byte[] image;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Timestamp joinDate;
}
