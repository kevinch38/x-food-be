package com.enigma.x_food.feature.merchant.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MerchantResponse {
    private String merchantID;
    private Timestamp joinDate;
    private String merchantName;
    private String picName;
    private String picNumber;
    private String picEmail;
    private String merchantDescription;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String merchantStatusID;
    private String notes;
}
