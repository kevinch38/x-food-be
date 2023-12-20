package com.enigma.x_food.feature.merchant;

import com.enigma.x_food.feature.merchant.dto.request.NewMerchantRequest;
import com.enigma.x_food.feature.merchant.dto.request.SearchMerchantRequest;
import com.enigma.x_food.feature.merchant.dto.request.UpdateMerchantRequest;
import com.enigma.x_food.feature.merchant.dto.response.MerchantResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MerchantService {
    MerchantResponse createNew(NewMerchantRequest request);
    MerchantResponse update(UpdateMerchantRequest request);
    MerchantResponse findById(String id);
    void deleteById(String id);
    List<MerchantResponse> getAllActive(SearchMerchantRequest request);
    Page<MerchantResponse> getAll(SearchMerchantRequest request);

}
