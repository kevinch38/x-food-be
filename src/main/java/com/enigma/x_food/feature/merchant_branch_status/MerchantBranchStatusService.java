package com.enigma.x_food.feature.merchant_branch_status;

import com.enigma.x_food.constant.EMerchantBranchStatus;
import com.enigma.x_food.feature.merchant_branch_status.response.MerchantBranchStatusResponse;

import java.util.List;

public interface MerchantBranchStatusService {
    MerchantBranchStatus getByStatus(EMerchantBranchStatus status);
    List<MerchantBranchStatusResponse> getAll();
}
