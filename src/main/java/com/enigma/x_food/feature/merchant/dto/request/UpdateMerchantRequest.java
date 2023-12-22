package com.enigma.x_food.feature.merchant.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateMerchantRequest {
    @NotBlank(message = "Merchant ID cannot be empty")
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
    @NotBlank(message = "Notes cannot be empty")
    private String notes;
    @NotNull(message = "Image cannot be empty")
    private MultipartFile image;
    @NotNull(message = "Logo image cannot be empty")
    private MultipartFile logoImage;
}
