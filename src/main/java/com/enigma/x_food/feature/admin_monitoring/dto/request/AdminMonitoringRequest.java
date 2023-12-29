package com.enigma.x_food.feature.admin_monitoring.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminMonitoringRequest {
    @NotBlank(message = "Activity name is required")
    private String activity;
    @NotBlank(message = "Admin ID is required")
    private String adminID;
}
