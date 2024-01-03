package com.enigma.x_food.feature.sub_variety;

import com.enigma.x_food.feature.sub_variety.dto.request.SubVarietyRequest;
import com.enigma.x_food.feature.sub_variety.dto.response.SubVarietyResponse;

import java.util.List;

public interface SubVarietyService {
    SubVarietyResponse createNew(SubVarietyRequest request);
    SubVarietyResponse findById(String id);
    SubVariety getById(String id);
    List<SubVarietyResponse> getAll();
}
