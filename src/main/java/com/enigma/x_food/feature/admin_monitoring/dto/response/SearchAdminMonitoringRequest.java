package com.enigma.x_food.feature.admin_monitoring.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
