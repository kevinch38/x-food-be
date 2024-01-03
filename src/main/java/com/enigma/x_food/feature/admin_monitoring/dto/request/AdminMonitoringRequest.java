package com.enigma.x_food.feature.admin_monitoring.dto.request;

import com.enigma.x_food.feature.admin.Admin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminMonitoringRequest {
    @NotBlank(message = "Activity name is required")
    private String activity;
    @NotNull(message = "Admin is required")
    private Admin admin;
}
