package com.enigma.x_food.feature.merchant.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateMerchantRequest {
    @NotBlank(message = "Merchant id cannot be empty")
    private String merchantID;
    @NotBlank(message = "Merchant name cannot be empty")
    private String merchantName;
    @NotBlank(message = "PIC name cannot be empty")
    private String picName;
    @NotBlank(message = "PIC number cannot be empty")
    private String picNumber;
    @NotBlank(message = "PIC email cannot be empty")
    private String picEmail;
    @NotBlank(message = "Merchant description cannot be empty")
    private String merchantDescription;
    @NotBlank(message = "Merchant status ID cannot be empty")
    private String merchantStatusID;
    private String notes;
}
