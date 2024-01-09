package com.enigma.x_food.feature.merchant_branch;

import com.enigma.x_food.feature.merchant_branch.dto.request.NewMerchantBranchRequest;
import com.enigma.x_food.feature.merchant_branch.dto.request.SearchActiveMerchantBranchRequest;
import com.enigma.x_food.feature.merchant_branch.dto.request.SearchMerchantBranchRequest;
import com.enigma.x_food.feature.merchant_branch.dto.request.UpdateMerchantBranchRequest;
import com.enigma.x_food.feature.merchant_branch.dto.response.MerchantBranchResponse;
import org.apache.tomcat.websocket.AuthenticationException;

import java.io.IOException;
import java.util.List;

public interface MerchantBranchService {
    MerchantBranchResponse createNew(NewMerchantBranchRequest request) throws IOException, AuthenticationException;
    MerchantBranchResponse update(UpdateMerchantBranchRequest request) throws IOException, AuthenticationException;
    void deleteById(String id) throws AuthenticationException;
    MerchantBranchResponse findById(String id);
    MerchantBranch getById(String id);
    List<MerchantBranchResponse> findAllByMerchantId(SearchMerchantBranchRequest request);
    List<MerchantBranchResponse> findAllActiveByMerchantId(SearchActiveMerchantBranchRequest request);
    void deleteApprove(String id);
    void approveToActive(String id);
    void rejectUpdate(String id);
}
