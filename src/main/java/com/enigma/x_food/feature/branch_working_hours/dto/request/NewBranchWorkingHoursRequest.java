package com.enigma.x_food.feature.branch_working_hours.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewBranchWorkingHoursRequest {
    @NotNull(message = "Open hour cannot be empty")
    private LocalTime openHour;
    @NotNull(message = "Close hour cannot be empty")
    private LocalTime closeHour;
    @NotNull(message = "Days cannot be empty")
    private String days;
}
