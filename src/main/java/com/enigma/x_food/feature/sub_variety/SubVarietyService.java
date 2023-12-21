package com.enigma.x_food.feature.sub_variety;

import com.enigma.x_food.feature.sub_variety.dto.request.SubVarietyRequest;

import java.util.List;

public interface SubVarietyService {
    SubVariety createNew(SubVarietyRequest request);
    SubVariety getById(String id);
    List<SubVariety> getAll();
}
