package com.enigma.x_food.feature.promotion;

import com.enigma.x_food.feature.promotion.dto.request.NewPromotionRequest;
import com.enigma.x_food.feature.promotion.dto.response.PromotionResponse;
import com.enigma.x_food.util.ValidationUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PromotionServiceImplTest {
    @Autowired
    private PromotionService promotionService;
    @MockBean
    private PromotionRepository promotionRepository;
    @MockBean
    private ValidationUtil validationUtil;
    @Test
    void createNew() {
        NewPromotionRequest newPromotionRequest = NewPromotionRequest.builder()
                .merchantID("1")
                .cost(100d)
                .maxRedeem(3)
                .promotionValue(200)
                .promotionDescription("tes")
                .promotionName("promotion tes")
                .quantity(2)
                .expiredDate(new Timestamp(System.currentTimeMillis()))
                .promotionStatusID("1")
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .build();
        Promotion promotion = Promotion.builder()
                .merchantID(newPromotionRequest.getMerchantID())
                .cost(newPromotionRequest.getCost())
                .maxRedeem(newPromotionRequest.getMaxRedeem())
                .promotionValue(newPromotionRequest.getPromotionValue())
                .promotionDescription(newPromotionRequest.getPromotionDescription())
                .promotionName(newPromotionRequest.getPromotionName())
                .quantity(newPromotionRequest.getQuantity())
                .adminID("adminID")
                .expiredDate(newPromotionRequest.getExpiredDate())
                .promotionStatusID(newPromotionRequest.getPromotionStatusID())
                .createdAt(newPromotionRequest.getCreatedAt())
                .updatedAt(newPromotionRequest.getUpdatedAt())
                .build();
        PromotionResponse promotionResponse = PromotionResponse.builder()
                .merchantID(promotion.getMerchantID())
                .cost(promotion.getCost())
                .maxRedeem(promotion.getMaxRedeem())
                .promotionValue(promotion.getPromotionValue())
                .promotionDescription(promotion.getPromotionDescription())
                .promotionName(promotion.getPromotionName())
                .quantity(promotion.getQuantity())
                .adminID("adminID")
                .expiredDate(promotion.getExpiredDate())
                .promotionStatusID(promotion.getPromotionStatusID())
                .createdAt(promotion.getCreatedAt())
                .updatedAt(promotion.getUpdatedAt())
                .build();
        Mockito.doNothing().when(validationUtil).validate(newPromotionRequest);
        Mockito.when(promotionRepository.saveAndFlush(promotion)).thenReturn(promotion);

        PromotionResponse actual = promotionService.createNew(newPromotionRequest);

        assertEquals("adminID",actual.getAdminID());
    }
}