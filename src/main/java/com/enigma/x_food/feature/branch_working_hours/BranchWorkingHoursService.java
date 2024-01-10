package com.enigma.x_food.feature.branch_working_hours;

import com.enigma.x_food.feature.branch_working_hours.dto.request.NewBranchWorkingHoursRequest;
import com.enigma.x_food.feature.branch_working_hours.dto.request.UpdateBranchWorkingHoursRequest;
import com.enigma.x_food.feature.branch_working_hours.dto.response.BranchWorkingHoursResponse;

import java.util.List;

public interface BranchWorkingHoursService {
    List<BranchWorkingHours> createNew(List<NewBranchWorkingHoursRequest> request);
    BranchWorkingHoursResponse update(UpdateBranchWorkingHoursRequest request);
    BranchWorkingHoursResponse findById(String id);
}
