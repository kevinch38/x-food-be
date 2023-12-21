package com.enigma.x_food.feature.branch_working_hours;

import com.enigma.x_food.feature.branch_working_hours.dto.request.NewBranchWorkingHoursRequest;
import com.enigma.x_food.feature.branch_working_hours.dto.request.UpdateBranchWorkingHoursRequest;
import com.enigma.x_food.feature.branch_working_hours.dto.response.BranchWorkingHoursResponse;

public interface BranchWorkingHoursService {
    BranchWorkingHours createNew(NewBranchWorkingHoursRequest request);
    BranchWorkingHoursResponse update(UpdateBranchWorkingHoursRequest request);
    BranchWorkingHoursResponse findById(String id);
}
