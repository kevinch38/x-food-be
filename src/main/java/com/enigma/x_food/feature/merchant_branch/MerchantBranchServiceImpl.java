package com.enigma.x_food.feature.merchant_branch;

import com.enigma.x_food.feature.merchant.Merchant;
import com.enigma.x_food.feature.merchant.MerchantService;
import com.enigma.x_food.feature.merchant.dto.response.MerchantResponse;
import com.enigma.x_food.feature.merchant_branch.dto.request.NewMerchantBranchRequest;
import com.enigma.x_food.feature.merchant_branch.dto.request.SearchMerchantBranchRequest;
import com.enigma.x_food.feature.merchant_branch.dto.request.UpdateMerchantBranchRequest;
import com.enigma.x_food.feature.merchant_branch.dto.response.MerchantBranchResponse;
import com.enigma.x_food.util.SortingUtil;
import com.enigma.x_food.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class MerchantBranchServiceImpl implements MerchantBranchService {
    private final MerchantBranchRepository merchantBranchRepository;
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
                .build();

        MerchantBranch branch = MerchantBranch.builder()
                .merchant(merchant)
                .branchName(request.getBranchName())
                .address(request.getAddress())
                .timezone(request.getTimezone())
                .branchWorkingHoursID(request.getBranchWorkingHoursID())
                .cityID(request.getCityID())
                .build();

        merchantBranchRepository.saveAndFlush(branch);
        return mapToResponse(branch);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MerchantBranchResponse update(UpdateMerchantBranchRequest request) {
        validationUtil.validate(request);
        MerchantBranch merchantBranch = findByIdOrThrowException(request.getMerchantBranchID());

        MerchantBranch updated = MerchantBranch.builder()
                .merchant(merchantBranch.getMerchant())
                .branchName(request.getBranchName())
                .address(request.getAddress())
                .timezone(request.getTimezone())
                .branchWorkingHoursID(request.getBranchWorkingHoursID())
                .cityID(request.getCityID())
                .build();

        return mapToResponse(merchantBranchRepository.saveAndFlush(updated));
    }

    @Override
    @Transactional(readOnly = true)
    public MerchantBranchResponse findById(String id) {
        validationUtil.validate(id);
        return mapToResponse(findByIdOrThrowException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MerchantBranchResponse> getAll(SearchMerchantBranchRequest request) {
        String fieldName = SortingUtil.sortByValidation(MerchantBranch.class, request.getSortBy(), "branchID");
        request.setSortBy(fieldName);

        Sort.Direction direction = Sort.Direction.fromString(request.getDirection());
        Pageable pageable = PageRequest.of(
                request.getPage() - 1,
                request.getSize(),
                direction,
                request.getSortBy()
        );

        Page<MerchantBranch> merchantBranches = merchantBranchRepository.findAll(pageable);
        return merchantBranches.map(this::mapToResponse);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String id) {
        validationUtil.validate(id);
        MerchantBranch merchantBranch = findByIdOrThrowException(id);
        merchantBranchRepository.delete(merchantBranch);
    }

    private MerchantBranch findByIdOrThrowException(String id) {
        return merchantBranchRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MerchantBranch not found"));
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
