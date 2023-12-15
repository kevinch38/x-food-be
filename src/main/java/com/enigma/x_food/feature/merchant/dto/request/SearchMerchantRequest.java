package com.enigma.x_food.feature.merchant.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchMerchantRequest {
    private Integer page;
    private Integer size;
    private String direction;
    private String sortBy;
    private String merchantID;
    private String merchantName;
    private String picName;
    private String picNumber;
    private String picEmail;
    private String merchantDescription;
    private String adminID;
    private String merchantStatusID;
    private String notes;
}
