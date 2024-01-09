package com.enigma.x_food.feature.promotion_update_request;

import com.enigma.x_food.feature.promotion.Promotion;
import com.enigma.x_food.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PromotionUpdateRequestServiceImpl implements PromotionUpdateRequestService {
    private final PromotionUpdateRequestRepository promotionUpdateRequestRepository;
    private final ValidationUtil validationUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PromotionUpdateRequest save(Promotion request) {
        validationUtil.validate(request);

        return promotionUpdateRequestRepository.saveAndFlush(getPromotionUpdateRequest(request));
    }

    @Override
    public Promotion getById(String id) {
        PromotionUpdateRequest promotionUpdateRequest = findByIdOrThrowException(id);
        return getPromotion(promotionUpdateRequest);
    }

    private PromotionUpdateRequest findByIdOrThrowException(String id) {
        return promotionUpdateRequestRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Merchant branch not found"));
    }

    private static Promotion getPromotion(PromotionUpdateRequest promotionUpdateRequest) {
        return Promotion.builder()
                .promotionID(promotionUpdateRequest.getPromotionID())
                .merchant(promotionUpdateRequest.getMerchant())
                .cost(promotionUpdateRequest.getCost())
                .maxRedeem(promotionUpdateRequest.getMaxRedeem())
                .promotionValue(promotionUpdateRequest.getPromotionValue())
                .promotionDescription(promotionUpdateRequest.getPromotionDescription())
                .promotionName(promotionUpdateRequest.getPromotionName())
                .quantity(promotionUpdateRequest.getQuantity())
                .expiredDate(promotionUpdateRequest.getExpiredDate())
                .admin(promotionUpdateRequest.getAdmin())
                .promotionStatus(promotionUpdateRequest.getPromotionStatus())
                .notes(promotionUpdateRequest.getNotes())
                .build();
    }

    private static PromotionUpdateRequest getPromotionUpdateRequest(Promotion promotion) {
        return PromotionUpdateRequest.builder()
                .promotionID(promotion.getPromotionID())
                .merchant(promotion.getMerchant())
                .cost(promotion.getCost())
                .maxRedeem(promotion.getMaxRedeem())
                .promotionValue(promotion.getPromotionValue())
                .promotionDescription(promotion.getPromotionDescription())
                .promotionName(promotion.getPromotionName())
                .quantity(promotion.getQuantity())
                .expiredDate(promotion.getExpiredDate())
                .admin(promotion.getAdmin())
                .promotionStatus(promotion.getPromotionStatus())
                .notes(promotion.getNotes())
                .build();
    }
}
