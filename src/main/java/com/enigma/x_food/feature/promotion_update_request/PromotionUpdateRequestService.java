package com.enigma.x_food.feature.promotion_update_request;

import com.enigma.x_food.feature.promotion.Promotion;

public interface PromotionUpdateRequestService {
    PromotionUpdateRequest save(Promotion request);
    Promotion getById(String id);
}
