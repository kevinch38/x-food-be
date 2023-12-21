package com.enigma.x_food.feature.branch_working_hours.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewBranchWorkingHoursRequest {
    @NotNull(message = "Open hour cannot be empty")
    private Timestamp openHour;
    @NotNull(message = "Close hour cannot be empty")
    private Timestamp closeHour;
    @NotNull(message = "Days cannot be empty")
    private Timestamp days;
    @NotNull(message = "Merchant branch ID cannot be empty")
    private String merchantBranchID;
}
