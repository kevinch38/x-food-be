package com.enigma.x_food.feature.merchant.service.impl;

import com.enigma.x_food.feature.merchant.dto.request.NewMerchantRequest;
import com.enigma.x_food.feature.merchant.dto.request.SearchMerchantRequest;
import com.enigma.x_food.feature.merchant.dto.response.MerchantResponse;
import com.enigma.x_food.feature.merchant.entity.Merchant;
import com.enigma.x_food.feature.merchant.repository.MerchantRepository;
import com.enigma.x_food.feature.merchant.service.MerchantService;
import com.enigma.x_food.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

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
                .merchantID(generateRandomId())
                .merchantName(request.getMerchantName())
                .merchantDescription(request.getMerchantDescription())
                .notes(request.getNotes())
                .joinDate(new Timestamp(System.currentTimeMillis()))
                .picName(request.getPicName())
                .picNumber(request.getPicNumber())
                .picEmail(request.getPicEmail())
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .build();
        merchantRepository.saveAndFlush(merchant);
        return mapToReponse(merchant);

    }


    @Override
    public Page<MerchantResponse> getAll(SearchMerchantRequest request) {
        return null;
    }

    private MerchantResponse mapToReponse(Merchant merchant) {
        return MerchantResponse.builder()
                .merchantID(merchant.getMerchantID())
                .merchantName(merchant.getMerchantName())
                .merchantDescription(merchant.getMerchantDescription())
                .notes(merchant.getNotes())
                .picName(merchant.getPicName())
                .picEmail(merchant.getPicEmail())
                .picNumber(merchant.getPicNumber())
                .joinDate(merchant.getJoinDate())
                .merchantStatusID("status")
                .merchantStatusID("1")
                .createdAt(merchant.getCreatedAt())
                .updatedAt(merchant.getUpdatedAt())
                .build();
    }

    private static String generateRandomId() {
        return RandomStringUtils.randomNumeric(6);
    }
}
