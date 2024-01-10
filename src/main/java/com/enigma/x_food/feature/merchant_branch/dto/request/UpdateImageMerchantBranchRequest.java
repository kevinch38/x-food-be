package com.enigma.x_food.feature.merchant_branch.dto.request;

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
public class UpdateImageMerchantBranchRequest {
    @NotBlank(message = "Merchant ID cannot be empty")
    private String merchantID;
    @NotNull(message = "Image cannot be empty")
    private MultipartFile image;
}