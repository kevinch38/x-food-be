package com.enigma.x_food.feature.branch_working_hours.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateBranchWorkingHoursRequest {
    @NotBlank(message = "Branch working hours ID cannot be empty")
    private String branchWorkingHoursID;
    @NotNull(message = "Open hour cannot be empty")
    private Timestamp openHour;
    @NotNull(message = "Close hour cannot be empty")
    private Timestamp closeHour;
    @NotNull(message = "Day cannot be empty")
    private Timestamp days;
    @NotBlank(message = "Merchant branch ID cannot be empty")
    private String merchantBranchID;
}
