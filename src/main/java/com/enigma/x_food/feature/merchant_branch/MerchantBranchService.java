package com.enigma.x_food.feature.merchant_branch;

import com.enigma.x_food.feature.merchant_branch.dto.request.NewMerchantBranchRequest;
import com.enigma.x_food.feature.merchant_branch.dto.request.SearchMerchantBranchRequest;
import com.enigma.x_food.feature.merchant_branch.dto.request.UpdateMerchantBranchRequest;
import com.enigma.x_food.feature.merchant_branch.dto.response.MerchantBranchResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MerchantBranchService {
    MerchantBranchResponse createNew(NewMerchantBranchRequest request);
    MerchantBranchResponse update(UpdateMerchantBranchRequest request);
    void deleteById(String id);
    MerchantBranchResponse findById(String id);
    List<MerchantBranchResponse> findByMerchantId(SearchMerchantBranchRequest request);
}
