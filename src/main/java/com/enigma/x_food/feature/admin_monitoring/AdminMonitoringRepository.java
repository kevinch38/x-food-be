package com.enigma.x_food.feature.admin_monitoring;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminMonitoringRepository extends JpaRepository<AdminMonitoring, String>, JpaSpecificationExecutor<AdminMonitoring> {
}
