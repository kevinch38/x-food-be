package com.enigma.x_food.feature.merchant_branch_update_request;

import com.enigma.x_food.feature.merchant_branch.MerchantBranch;
import com.enigma.x_food.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class MerchantBranchUpdateRequestServiceImpl implements MerchantBranchUpdateRequestService {
    private final MerchantBranchUpdateRequestRepository merchantBranchUpdateRequestRepository;
    private final ValidationUtil validationUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MerchantBranchUpdateRequest save(MerchantBranch request) {
        validationUtil.validate(request);

        return merchantBranchUpdateRequestRepository.saveAndFlush(getMerchantBranchUpdateRequest(request));
    }

    @Override
    public MerchantBranch getById(String id) {
        MerchantBranchUpdateRequest merchantBranchUpdateRequest = findByIdOrThrowException(id);
        return getMerchantBranch(merchantBranchUpdateRequest);
    }

    private MerchantBranchUpdateRequest findByIdOrThrowException(String id) {
        return merchantBranchUpdateRequestRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Merchant branch not found"));
    }

    private static MerchantBranch getMerchantBranch(MerchantBranchUpdateRequest merchantBranchUpdateRequest) {
        return MerchantBranch.builder()
                .branchID(merchantBranchUpdateRequest.getBranchID())
                .merchant(merchantBranchUpdateRequest.getMerchant())
                .branchName(merchantBranchUpdateRequest.getBranchName())
                .address(merchantBranchUpdateRequest.getAddress())
                .timezone(merchantBranchUpdateRequest.getTimezone())
                .picName(merchantBranchUpdateRequest.getPicName())
                .picNumber(merchantBranchUpdateRequest.getPicNumber())
                .picEmail(merchantBranchUpdateRequest.getPicEmail())
                .admin(merchantBranchUpdateRequest.getAdmin())
                .joinDate(merchantBranchUpdateRequest.getJoinDate())
                .image(merchantBranchUpdateRequest.getImage())
                .city(merchantBranchUpdateRequest.getCity())
                .items(merchantBranchUpdateRequest.getItems())
                .merchantBranchStatus(merchantBranchUpdateRequest.getMerchantBranchStatus())
                .subVarieties(merchantBranchUpdateRequest.getSubVarieties())
                .build();
    }

    private static MerchantBranchUpdateRequest getMerchantBranchUpdateRequest(MerchantBranch merchantBranch) {
        return MerchantBranchUpdateRequest.builder()
                .branchID(merchantBranch.getBranchID())
                .merchant(merchantBranch.getMerchant())
                .branchName(merchantBranch.getBranchName())
                .address(merchantBranch.getAddress())
                .timezone(merchantBranch.getTimezone())
                .picName(merchantBranch.getPicName())
                .picNumber(merchantBranch.getPicNumber())
                .picEmail(merchantBranch.getPicEmail())
                .admin(merchantBranch.getAdmin())
                .joinDate(merchantBranch.getJoinDate())
                .image(merchantBranch.getImage())
                .city(merchantBranch.getCity())
                .items(merchantBranch.getItems())
                .merchantBranchStatus(merchantBranch.getMerchantBranchStatus())
                .subVarieties(merchantBranch.getSubVarieties())
                .build();
    }
}
