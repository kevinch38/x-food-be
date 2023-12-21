package com.enigma.x_food.feature.variety;

import java.util.List;

public interface VarietyService {
    Variety createNew(Variety request);
    Variety getById(String id);
    List<Variety> getAll();
}
