package com.enigma.x_food.feature.merchant;

import com.enigma.x_food.feature.merchant.dto.request.NewMerchantRequest;
import com.enigma.x_food.feature.merchant.dto.request.SearchMerchantRequest;
import com.enigma.x_food.feature.merchant.dto.request.UpdateMerchantRequest;
import com.enigma.x_food.feature.merchant.dto.response.MerchantResponse;
import com.enigma.x_food.feature.merchant_branch.MerchantBranch;
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

@Service
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {
    private final MerchantRepository merchantRepository;
    private final ValidationUtil validationUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MerchantResponse createNew(NewMerchantRequest request) {
        validationUtil.validate(request);
        Merchant merchant = Merchant.builder()
                .joinDate(request.getJoinDate())
                .merchantName(request.getMerchantName())
                .picName(request.getPicName())
                .picNumber(request.getPicNumber())
                .picEmail(request.getPicEmail())
                .merchantDescription(request.getMerchantDescription())
                .adminID("")
                .merchantStatusID(request.getMerchantStatusID())
                .notes(request.getNotes())
                .build();
        merchantRepository.saveAndFlush(merchant);
        return mapToResponse(merchant);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MerchantResponse update(UpdateMerchantRequest request) {
        validationUtil.validate(request);
        Merchant merchant = findByIdOrThrowException(request.getMerchantID());

        Merchant updated = Merchant.builder()
                .merchantID(merchant.getMerchantID())
                .joinDate(merchant.getJoinDate())
                .merchantName(request.getMerchantName())
                .picName(request.getPicName())
                .picNumber(request.getPicNumber())
                .picEmail(request.getPicEmail())
                .merchantDescription(request.getMerchantDescription())
                .adminID(merchant.getAdminID())
                .merchantStatusID(request.getMerchantStatusID())
                .notes(request.getNotes())
                .build();

        return mapToResponse(merchantRepository.saveAndFlush(updated));
    }

    @Override
    @Transactional(readOnly = true)
    public MerchantResponse findById(String id) {
        validationUtil.validate(id);
        return mapToResponse(findByIdOrThrowException(id));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String id) {
        validationUtil.validate(id);
        Merchant merchant = findByIdOrThrowException(id);
        merchantRepository.delete(merchant);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MerchantResponse> getAll(SearchMerchantRequest request) {
        String fieldName = SortingUtil.sortByValidation(Merchant.class, request.getSortBy(), "merchantID");
        request.setSortBy(fieldName);

        Sort.Direction direction = Sort.Direction.fromString(request.getDirection());
        Pageable pageable = PageRequest.of(
                request.getPage() - 1,
                request.getSize(),
                direction,
                request.getSortBy()
        );

        Page<Merchant> merchantBranches = merchantRepository.findAll(pageable);
        return merchantBranches.map(this::mapToResponse);
    }

    private MerchantResponse mapToResponse(Merchant merchant) {
        return MerchantResponse.builder()
                .merchantID(merchant.getMerchantID())
                .joinDate(merchant.getJoinDate())
                .merchantName(merchant.getMerchantName())
                .picName(merchant.getPicName())
                .picNumber(merchant.getPicNumber())
                .picEmail(merchant.getPicEmail())
                .merchantDescription(merchant.getMerchantDescription())
                .adminId(merchant.getAdminID())
                .createdAt(merchant.getCreatedAt())
                .updatedAt(merchant.getUpdatedAt())
                .merchantStatusID(merchant.getMerchantStatusID())
                .notes(merchant.getNotes())
                .build();
    }

    private Merchant findByIdOrThrowException(String id) {
        return merchantRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Merchant not found")
        );
    }
}
