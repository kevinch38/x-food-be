package com.enigma.x_food.feature.merchant.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewMerchantRequest {
    @NotNull(message = "Join date cannot be empty")
    private Timestamp joinDate;
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
    @NotNull(message = "Image cannot be empty")
    private MultipartFile image;
    @NotNull(message = "Logo image cannot be empty")
    private MultipartFile logoImage;
    private String notes;
}
