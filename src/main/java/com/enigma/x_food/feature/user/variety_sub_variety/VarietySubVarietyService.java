package com.enigma.x_food.feature.user.variety_sub_variety;

import com.enigma.x_food.feature.user.variety_sub_variety.dto.request.VarietySubVarietyRequest;
import com.enigma.x_food.feature.user.variety_sub_variety.dto.response.VarietySubVarietyResponse;

import java.util.List;

public interface VarietySubVarietyService {
    VarietySubVarietyResponse createNew(VarietySubVarietyRequest request);
    VarietySubVarietyResponse getById(String id);
    VarietySubVariety findById(String id);
    List<VarietySubVariety> getAll();
}
