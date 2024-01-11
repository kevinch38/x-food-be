package com.enigma.x_food.feature.merchant_branch;

import com.enigma.x_food.feature.merchant_branch.dto.request.*;
import com.enigma.x_food.feature.merchant_branch.dto.response.MerchantBranchResponse;
import org.apache.tomcat.websocket.AuthenticationException;
import org.hibernate.sql.Update;
import org.springframework.web.multipart.MultipartFile;

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
    void deleteApprove(String id) throws AuthenticationException;
    void approveToActive(String id) throws AuthenticationException;
    void rejectUpdate(String id) throws AuthenticationException;
    MerchantBranchResponse updateImage(UpdateImageMerchantBranchRequest request) throws IOException;
}
