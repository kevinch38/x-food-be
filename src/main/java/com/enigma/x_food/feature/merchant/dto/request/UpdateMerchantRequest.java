package com.enigma.x_food.feature.merchant.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateMerchantRequest {
    @NotBlank(message = "Merchant ID cannot be empty")
    private String merchantID;
    private String merchantName;
    private String picName;
    private String picNumber;
    private String picEmail;
    private String merchantDescription;
    private String notes;
    private MultipartFile image;
    private MultipartFile logoImage;
}
