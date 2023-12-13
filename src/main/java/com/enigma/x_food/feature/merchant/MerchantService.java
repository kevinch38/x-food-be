package com.enigma.x_food.feature.merchant;

import com.enigma.x_food.feature.merchant.dto.request.NewMerchantRequest;
import com.enigma.x_food.feature.merchant.dto.request.SearchMerchantRequest;
import com.enigma.x_food.feature.merchant.dto.response.MerchantResponse;
import org.springframework.data.domain.Page;

public interface MerchantService {
    MerchantResponse createNew(NewMerchantRequest request);
    Page<MerchantResponse> getAll(SearchMerchantRequest request);
}
