package com.enigma.x_food.feature.merchant_branch_update_request.dto.request;

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
public class MerchantBranchRequest {
    @NotBlank(message = "Merchant ID cannot be empty")
    private String merchantID;
    @NotBlank(message = "Branch name cannot be empty")
    private String branchName;
    @NotBlank(message = "Address cannot be empty")
    private String address;
    @NotBlank(message = "Timezone cannot be empty")
    private String timezone;
    @NotBlank(message = "Branch working hours ID cannot be empty")
    private String branchWorkingHoursID;
    @NotBlank(message = "City ID cannot be empty")
    private String cityID;
    @NotNull(message = "Image cannot be empty")
    private MultipartFile image;
    @NotBlank(message = "PIC name cannot be empty")
    private String picName;
    @NotBlank(message = "PIC number cannot be empty")
    private String picNumber;
    @NotBlank(message = "PIC email cannot be empty")
    private String picEmail;
    @NotNull(message = "Join date cannot be empty")
    private Timestamp joinDate;
}