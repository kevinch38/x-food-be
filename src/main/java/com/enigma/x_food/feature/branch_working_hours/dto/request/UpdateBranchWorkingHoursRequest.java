package com.enigma.x_food.feature.branch_working_hours.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateBranchWorkingHoursRequest {
    @NotBlank(message = "Branch working hours ID cannot be empty")
    private String branchWorkingHoursID;
    private Timestamp openHour;
    private Timestamp closeHour;
    private Timestamp days;
    private String merchantBranchID;
}
