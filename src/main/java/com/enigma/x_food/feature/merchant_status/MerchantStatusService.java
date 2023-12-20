package com.enigma.x_food.feature.merchant_status;

import com.enigma.x_food.constant.EMerchantStatus;
import com.enigma.x_food.feature.merchant_status.response.MerchantStatusResponse;

import java.util.List;

public interface MerchantStatusService {
    MerchantStatus getByStatus(EMerchantStatus status);
    List<MerchantStatusResponse> getAll();
}
