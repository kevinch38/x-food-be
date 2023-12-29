package com.enigma.x_food.feature.loyalty_point;

import com.enigma.x_food.feature.loyalty_point.dto.request.NewLoyaltyPointRequest;
import com.enigma.x_food.feature.loyalty_point.dto.request.UpdateLoyaltyPointRequest;
import com.enigma.x_food.feature.loyalty_point.dto.response.LoyaltyPointResponse;

public interface LoyaltyPointService {
    LoyaltyPoint createNew(NewLoyaltyPointRequest request);
    LoyaltyPointResponse update(UpdateLoyaltyPointRequest request);
    LoyaltyPoint getById(String id);
    LoyaltyPointResponse findById(String id);
}
