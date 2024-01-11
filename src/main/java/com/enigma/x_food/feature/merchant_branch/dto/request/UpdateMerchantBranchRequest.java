package com.enigma.x_food.feature.merchant_branch.dto.request;

import com.enigma.x_food.feature.branch_working_hours.dto.request.UpdateBranchWorkingHoursRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateMerchantBranchRequest {
    @NotBlank(message = "Branch ID cannot be empty")
    private String branchID;
    @NotBlank(message = "Branch name cannot be empty")
    private String branchName;
    @NotBlank(message = "Address cannot be empty")
    private String address;
    @NotBlank(message = "Timezone cannot be empty")
    private String timezone;
    @NotNull(message = "Branch working hours cannot be empty")
    private List<UpdateBranchWorkingHoursRequest> branchWorkingHours;
    @NotBlank(message = "City ID cannot be empty")
    private String cityID;
    @NotBlank(message = "PIC name cannot be empty")
    private String picName;
    @NotBlank(message = "PIC number cannot be empty")
    private String picNumber;
    @NotBlank(message = "PIC email cannot be empty")
    private String picEmail;
}
