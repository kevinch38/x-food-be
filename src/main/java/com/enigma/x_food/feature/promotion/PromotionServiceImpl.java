package com.enigma.x_food.feature.promotion;

import com.enigma.x_food.feature.merchant.Merchant;
import com.enigma.x_food.feature.merchant.MerchantService;
import com.enigma.x_food.feature.merchant.dto.response.MerchantResponse;
import com.enigma.x_food.feature.promotion.dto.request.NewPromotionRequest;
import com.enigma.x_food.feature.promotion.dto.request.SearchPromotionRequest;
import com.enigma.x_food.feature.promotion.dto.request.UpdatePromotionRequest;
import com.enigma.x_food.feature.promotion.dto.response.PromotionResponse;
import com.enigma.x_food.util.SortingUtil;
import com.enigma.x_food.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;

@Service
@RequiredArgsConstructor
@Slf4j
public class PromotionServiceImpl implements PromotionService {
    private final PromotionRepository promotionRepository;
    private final MerchantService merchantService;
    private final ValidationUtil validationUtil;
    private final EntityManager entityManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PromotionResponse createNew(NewPromotionRequest request) {
        try {
            log.info("Start createNew");
            validationUtil.validate(request);
            MerchantResponse merchantResponse = merchantService.findById(request.getMerchantID());
            Merchant merchant = Merchant.builder()
                    .merchantID(merchantResponse.getMerchantID())
                    .joinDate(merchantResponse.getJoinDate())
                    .merchantName(merchantResponse.getMerchantName())
                    .picName(merchantResponse.getPicName())
                    .picNumber(merchantResponse.getPicNumber())
                    .picEmail(merchantResponse.getPicEmail())
                    .merchantDescription(merchantResponse.getMerchantDescription())
                    .adminID(merchantResponse.getAdminId())
                    .createdAt(merchantResponse.getCreatedAt())
                    .updatedAt(merchantResponse.getUpdatedAt())
                    .merchantStatusID(merchantResponse.getMerchantStatusID())
                    .notes(merchantResponse.getNotes())
                    .build();

            Promotion promotion = Promotion.builder()
                    .merchant(entityManager.merge(merchant))
                    .cost(request.getCost())
                    .maxRedeem(request.getMaxRedeem())
                    .promotionValue(request.getPromotionValue())
                    .promotionDescription(request.getPromotionDescription())
                    .promotionName(request.getPromotionName())
                    .quantity(request.getQuantity())
                    .adminID("adminID")
                    .expiredDate(request.getExpiredDate())
                    .promotionStatusID(request.getPromotionStatusID())
                    .notes(request.getNotes())
                    .build();
            promotionRepository.saveAndFlush(promotion);
            log.info("End createNew");
            return mapToResponse(promotion);
        } catch (DataIntegrityViolationException e) {
            log.error("Error createNew: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "promotion already exist");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PromotionResponse update(UpdatePromotionRequest request) {
        validationUtil.validate(request);
        Promotion promotion = findByIdOrThrowException(request.getPromotionID());

        MerchantResponse merchantResponse = merchantService.findById(request.getMerchantID());
        Merchant merchant = Merchant.builder()
                .merchantID(merchantResponse.getMerchantID())
                .joinDate(merchantResponse.getJoinDate())
                .merchantName(merchantResponse.getMerchantName())
                .picName(merchantResponse.getPicName())
                .picNumber(merchantResponse.getPicNumber())
                .picEmail(merchantResponse.getPicEmail())
                .merchantDescription(merchantResponse.getMerchantDescription())
                .adminID(merchantResponse.getAdminId())
                .createdAt(merchantResponse.getCreatedAt())
                .updatedAt(merchantResponse.getUpdatedAt())
                .merchantStatusID(merchantResponse.getMerchantStatusID())
                .notes(merchantResponse.getNotes())
                .build();

        Promotion updated = Promotion.builder()
                .promotionID(promotion.getPromotionID())
                .merchant(entityManager.merge(merchant))
                .cost(request.getCost())
                .maxRedeem(request.getMaxRedeem())
                .promotionValue(request.getPromotionValue())
                .promotionDescription(request.getPromotionDescription())
                .promotionName(request.getPromotionName())
                .quantity(request.getQuantity())
                .adminID(promotion.getAdminID())
                .expiredDate(request.getExpiredDate())
                .promotionStatusID(request.getPromotionStatusID())
                .notes(request.getNotes())
                .build();

        return mapToResponse(promotionRepository.saveAndFlush(updated));
    }

    @Override
    @Transactional(readOnly = true)
    public PromotionResponse findById(String id) {
        validationUtil.validate(id);
        return mapToResponse(findByIdOrThrowException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PromotionResponse> getAll(SearchPromotionRequest request) {
        String fieldName = SortingUtil.sortByValidation(Promotion.class, request.getSortBy(), "promotionID");
        request.setSortBy(fieldName);

        Sort.Direction direction = Sort.Direction.fromString(request.getDirection());
        Pageable pageable = PageRequest.of(
                request.getPage() - 1,
                request.getSize(),
                direction,
                request.getSortBy()
        );

        Page<Promotion> promotions = promotionRepository.findAll(pageable);
        return promotions.map(this::mapToResponse);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String id) {
        validationUtil.validate(id);
        Promotion promotion = findByIdOrThrowException(id);
        promotionRepository.delete(promotion);
    }

    private Promotion findByIdOrThrowException(String id) {
        return promotionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Promotion not found"));
    }

    private PromotionResponse mapToResponse(Promotion promotion) {
        return PromotionResponse.builder()
                .promotionID(promotion.getPromotionID())
                .merchantID(promotion.getMerchant().getMerchantID())
                .cost(promotion.getCost())
                .maxRedeem(promotion.getMaxRedeem())
                .promotionValue(promotion.getPromotionValue())
                .promotionDescription(promotion.getPromotionDescription())
                .promotionName(promotion.getPromotionName())
                .quantity(promotion.getQuantity())
                .adminID(promotion.getAdminID())
                .expiredDate(promotion.getExpiredDate())
                .promotionStatusID(promotion.getPromotionStatusID())
                .createdAt(promotion.getCreatedAt())
                .updatedAt(promotion.getUpdatedAt())
                .notes(promotion.getNotes())
                .build();
    }
}
