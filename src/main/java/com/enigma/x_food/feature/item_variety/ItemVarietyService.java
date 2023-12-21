package com.enigma.x_food.feature.item_variety;

import com.enigma.x_food.feature.item_variety.dto.request.ItemVarietyRequest;
import com.enigma.x_food.feature.item_variety.dto.response.ItemVarietyResponse;

import java.util.List;

public interface ItemVarietyService {
    ItemVarietyResponse createNew(ItemVarietyRequest request);
    ItemVarietyResponse getById(String id);
    List<ItemVariety> getAll();
}
