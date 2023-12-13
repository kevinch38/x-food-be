package com.enigma.x_food.feature.merchant_branch;

import com.enigma.x_food.feature.merchant_branch.dto.request.NewMerchantBranchRequest;
import com.enigma.x_food.feature.merchant_branch.dto.request.SearchMerchantBranchRequest;
import com.enigma.x_food.feature.merchant_branch.dto.response.MerchantBranchResponse;
import org.springframework.data.domain.Page;

public interface MerchantBranchService {
    MerchantBranchResponse createNew(NewMerchantBranchRequest request);
    Page<MerchantBranchResponse> getAll(SearchMerchantBranchRequest request);
}
