package com.enigma.x_food.feature.promotion;

import com.enigma.x_food.feature.promotion.dto.request.NewPromotionRequest;
import com.enigma.x_food.feature.promotion.dto.request.SearchPromotionRequest;
import com.enigma.x_food.feature.promotion.dto.request.UpdatePromotionRequest;
import com.enigma.x_food.feature.promotion.dto.response.PromotionResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PromotionService {
    PromotionResponse createNew(NewPromotionRequest request);
    PromotionResponse update(UpdatePromotionRequest request);
    void deleteById(String id);
    PromotionResponse findById(String id);
    Promotion getPromotionById(String id);
    Page<PromotionResponse> getAll(SearchPromotionRequest request);
    List<PromotionResponse> getAllActive(SearchPromotionRequest request);
}
