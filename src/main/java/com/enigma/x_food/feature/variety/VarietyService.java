package com.enigma.x_food.feature.variety;

import com.enigma.x_food.feature.variety.request.VarietyRequest;

import java.util.List;

public interface VarietyService {
    Variety createNew(VarietyRequest request);
    Variety getById(String id);
    List<Variety> getAll();
}
