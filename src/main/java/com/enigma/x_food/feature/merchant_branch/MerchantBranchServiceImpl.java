package com.enigma.x_food.feature.merchant_branch;

import com.enigma.x_food.feature.merchant_branch.dto.request.NewMerchantBranchRequest;
import com.enigma.x_food.feature.merchant_branch.dto.request.SearchMerchantBranchRequest;
import com.enigma.x_food.feature.merchant_branch.dto.response.MerchantBranchResponse;
import com.enigma.x_food.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class MerchantBranchServiceImpl implements MerchantBranchService {
    private final MerchantBranchRepository merchantRepository;
    private final ValidationUtil validationUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MerchantBranchResponse createNew(NewMerchantBranchRequest request) {
        validationUtil.validate(request);
        MerchantBranch branch = MerchantBranch.builder()
                .branchID(generateRandomId())
                .merchantID(request.getMerchantID())
                .branchName(request.getBranchName())
                .address(request.getAddress())
                .branchWorkingHoursID(request.getBranchWorkingHoursID())
                .timezone(request.getTimezone())
                .cityID(request.getCityID())
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .build();


        merchantRepository.saveAndFlush(branch);
        return mapToReponse(branch);

    }


    @Override
    public Page<MerchantBranchResponse> getAll(SearchMerchantBranchRequest request) {
        return null;
    }

    private MerchantBranchResponse mapToReponse(MerchantBranch branch) {
        return MerchantBranchResponse.builder()
                .branchID(branch.getBranchID())
                .cityID(branch.getCityID())
                .branchName(branch.getBranchName())
                .branchWorkingHoursID(branch.getBranchWorkingHoursID())
                .timezone(branch.getTimezone())
                .address(branch.getAddress())
                .merchantID(branch.getMerchantID())
                .updatedAt(branch.getUpdatedAt())
                .createdAt(branch.getCreatedAt())
                .build();
    }

    private static String generateRandomId() {
        return RandomStringUtils.randomNumeric(6);
    }
}
