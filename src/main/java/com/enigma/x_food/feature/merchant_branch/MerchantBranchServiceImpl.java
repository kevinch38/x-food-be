package com.enigma.x_food.feature.merchant_branch;

import com.enigma.x_food.feature.merchant.Merchant;
import com.enigma.x_food.feature.merchant.MerchantService;
import com.enigma.x_food.feature.merchant.dto.response.MerchantResponse;
import com.enigma.x_food.feature.merchant_branch.dto.request.NewMerchantBranchRequest;
import com.enigma.x_food.feature.merchant_branch.dto.request.SearchMerchantBranchRequest;
import com.enigma.x_food.feature.merchant_branch.dto.response.MerchantBranchResponse;
import com.enigma.x_food.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class MerchantBranchServiceImpl implements MerchantBranchService {
    private final MerchantBranchRepository merchantRepository;
    private final MerchantService merchantService;
    private final ValidationUtil validationUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MerchantBranchResponse createNew(NewMerchantBranchRequest request) {
        validationUtil.validate(request);
        MerchantResponse merchantResponse = merchantService.findById(request.getMerchantID());
        Merchant merchant = Merchant.builder()
                .merchantID(merchantResponse.getMerchantID())
                .merchantName(merchantResponse.getMerchantName())
                .merchantDescription(merchantResponse.getMerchantDescription())
                .notes(merchantResponse.getNotes())
                .picName(merchantResponse.getPicName())
                .picEmail(merchantResponse.getPicEmail())
                .picNumber(merchantResponse.getPicNumber())
                .joinDate(merchantResponse.getJoinDate())
                .merchantStatusID(merchantResponse.getMerchantStatusID())
                .createdAt(merchantResponse.getCreatedAt())
                .updatedAt(merchantResponse.getUpdatedAt())
                .build();

        MerchantBranch branch = MerchantBranch.builder()
                .merchant(merchant)
                .branchName(request.getBranchName())
                .address(request.getAddress())
                .timezone(request.getTimezone())
                .branchWorkingHoursID(request.getBranchWorkingHoursID())
                .cityID(request.getCityID())
                .build();

        merchantRepository.saveAndFlush(branch);
        return mapToResponse(branch);
    }

    @Override
    public Page<MerchantBranchResponse> getAll(SearchMerchantBranchRequest request) {
        return null;
    }

    private MerchantBranchResponse mapToResponse(MerchantBranch branch) {
        return MerchantBranchResponse.builder()
                .branchID(branch.getBranchID())
                .merchantID(branch.getMerchant().getMerchantID())
                .branchName(branch.getBranchName())
                .address(branch.getAddress())
                .timezone(branch.getTimezone())
                .createdAt(branch.getCreatedAt())
                .updatedAt(branch.getUpdatedAt())
                .branchWorkingHoursID(branch.getBranchWorkingHoursID())
                .cityID(branch.getCityID())
                .build();
    }
}
