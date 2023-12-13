package com.enigma.x_food.feature.merchant.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewMerchantRequest {
    private String merchantName;
    private String picName;
    private String picNumber;
    private String picEmail;
    private String merchantDescription;
    private String merchantStatusID;
    private String notes;
}
