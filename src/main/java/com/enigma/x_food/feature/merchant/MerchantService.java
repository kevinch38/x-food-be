package com.enigma.x_food.feature.merchant;

import com.enigma.x_food.feature.merchant.dto.request.NewMerchantRequest;
import com.enigma.x_food.feature.merchant.dto.request.SearchActiveMerchantRequest;
import com.enigma.x_food.feature.merchant.dto.request.SearchMerchantRequest;
import com.enigma.x_food.feature.merchant.dto.request.UpdateMerchantRequest;
import com.enigma.x_food.feature.merchant.dto.response.MerchantResponse;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;

public interface MerchantService {
    MerchantResponse createNew(NewMerchantRequest request) throws IOException, AuthenticationException;
    void approveToActive(String id);
    MerchantResponse update(UpdateMerchantRequest request) throws IOException, AuthenticationException;
    MerchantResponse findById(String id);
    Merchant getById(String id);
    void deleteById(String id) throws AuthenticationException;
    void deleteApprove(String id);
    List<MerchantResponse> getAllActive(SearchActiveMerchantRequest request);
    Page<MerchantResponse> getAll(SearchMerchantRequest request);

}
