package com.enigma.x_food.feature.promotion_status;

import com.enigma.x_food.constant.EMerchantStatus;
import com.enigma.x_food.constant.EPromotionStatus;
import com.enigma.x_food.feature.promotion_status.response.PromotionStatusResponse;

import java.util.List;

public interface PromotionStatusService {
    PromotionStatus getByStatus(EPromotionStatus status);
    List<PromotionStatusResponse> getAll();
}
