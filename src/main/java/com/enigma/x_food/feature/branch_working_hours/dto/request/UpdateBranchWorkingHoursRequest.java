package com.enigma.x_food.feature.branch_working_hours.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateBranchWorkingHoursRequest {
    @NotBlank(message = "Branch working hours ID cannot be empty")
    private String branchWorkingHoursID;
    @NotNull(message = "Open hour cannot be empty")
    private LocalTime openHour;
    @NotNull(message = "Close hour cannot be empty")
    private LocalTime closeHour;
    @NotNull(message = "Day cannot be empty")
    private String days;
    @NotBlank(message = "Merchant branch ID cannot be empty")
    private String merchantBranchID;
}
