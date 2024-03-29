package com.enigma.x_food.feature.item;

import com.enigma.x_food.feature.item.dto.request.NewItemRequest;
import com.enigma.x_food.feature.item.dto.request.SearchItemRequest;
import com.enigma.x_food.feature.item.dto.response.ItemResponse;

import java.io.IOException;
import java.util.List;

public interface ItemService {
    void deleteById(String id);
    Item findById(String id);
    List<ItemResponse> getAll(SearchItemRequest request);
    ItemResponse createNew(NewItemRequest request) throws IOException;
}
