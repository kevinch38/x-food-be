package com.enigma.x_food.feature.admin_monitoring.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchAdminMonitoringRequest {
    private Integer page;
    private Integer size;
    private String direction;
    private String sortBy;
    private String adminName;
    private LocalDate startUpdatedAt;
    private LocalDate endUpdatedAt;
    private String adminRole;
    private String activity;
}
