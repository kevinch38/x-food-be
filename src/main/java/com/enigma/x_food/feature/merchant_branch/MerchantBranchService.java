package com.enigma.x_food.feature.merchant_branch;

import com.enigma.x_food.feature.merchant_branch.dto.request.NewMerchantBranchRequest;
import com.enigma.x_food.feature.merchant_branch.dto.request.SearchMerchantBranchRequest;
import com.enigma.x_food.feature.merchant_branch.dto.request.UpdateMerchantBranchRequest;
import com.enigma.x_food.feature.merchant_branch.dto.response.MerchantBranchResponse;

import java.io.IOException;
import java.util.List;

public interface MerchantBranchService {
    MerchantBranchResponse createNew(NewMerchantBranchRequest request) throws IOException;
    MerchantBranchResponse update(UpdateMerchantBranchRequest request) throws IOException;
    void deleteById(String id);
    MerchantBranch findById(String id);
//    List<MerchantBranchResponse> findAllByMerchantId(SearchMerchantBranchRequest request);
    List<MerchantBranchResponse> getAllActiveByMerchantId(SearchMerchantBranchRequest request);
}
