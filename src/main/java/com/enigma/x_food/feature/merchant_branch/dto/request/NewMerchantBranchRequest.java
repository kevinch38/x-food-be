package com.enigma.x_food.feature.merchant_branch.dto.request;

import com.enigma.x_food.feature.branch_working_hours.dto.request.NewBranchWorkingHoursRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewMerchantBranchRequest {
    @NotBlank(message = "Merchant ID cannot be empty")
    private String merchantID;
    @NotBlank(message = "Branch name cannot be empty")
    private String branchName;
    @NotBlank(message = "Address cannot be empty")
    private String address;
    @NotBlank(message = "Timezone cannot be empty")
    private String timezone;
    @NotBlank(message = "Branch working hours ID cannot be empty")
    private List<NewBranchWorkingHoursRequest> branchWorkingHours;
    @NotBlank(message = "City ID cannot be empty")
    private String cityID;
    @NotBlank(message = "PIC name cannot be empty")
    private String picName;
    @NotBlank(message = "PIC number cannot be empty")
    private String picNumber;
    @NotBlank(message = "PIC email cannot be empty")
    private String picEmail;
    @NotNull(message = "Join date cannot be empty")
    private Timestamp joinDate;
}