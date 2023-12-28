package com.enigma.x_food.feature.merchant;

import com.enigma.x_food.feature.merchant.dto.request.NewMerchantRequest;
import com.enigma.x_food.feature.merchant.dto.request.SearchMerchantRequest;
import com.enigma.x_food.feature.merchant.dto.request.UpdateMerchantRequest;
import com.enigma.x_food.feature.merchant.dto.response.MerchantResponse;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;

public interface MerchantService {
    MerchantResponse createNew(NewMerchantRequest request) throws IOException;
    MerchantResponse update(UpdateMerchantRequest request) throws IOException;
    MerchantResponse findById(String id);
    Merchant getById(String id);
    void deleteById(String id);
    List<MerchantResponse> getAllActive(SearchMerchantRequest request);
    Page<MerchantResponse> getAll(SearchMerchantRequest request);

}
