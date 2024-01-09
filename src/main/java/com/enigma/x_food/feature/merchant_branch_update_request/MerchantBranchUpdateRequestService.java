package com.enigma.x_food.feature.merchant_branch_update_request;

import com.enigma.x_food.feature.merchant_branch.MerchantBranch;

public interface MerchantBranchUpdateRequestService {
    MerchantBranchUpdateRequest save(MerchantBranch request);
    MerchantBranch getById(String id);
}
