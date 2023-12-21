package com.enigma.x_food.feature.branch_working_hours;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchWorkingHoursRepository extends JpaRepository<BranchWorkingHours, String>, JpaSpecificationExecutor<BranchWorkingHours> {
}
