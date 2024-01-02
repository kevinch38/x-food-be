package com.enigma.x_food.feature.admin_monitoring;

import com.enigma.x_food.feature.admin_monitoring.dto.request.AdminMonitoringRequest;
import com.enigma.x_food.feature.admin_monitoring.dto.response.AdminMonitoringResponse;
import com.enigma.x_food.feature.admin_monitoring.dto.request.SearchAdminMonitoringRequest;
import org.springframework.data.domain.Page;

public interface AdminMonitoringService {
    AdminMonitoringResponse createNew(AdminMonitoringRequest request);
    AdminMonitoringResponse findById(String id);
    AdminMonitoring getById(String id);
    Page<AdminMonitoringResponse> findAll(SearchAdminMonitoringRequest request);
}
