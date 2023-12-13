package com.enigma.x_food.feature.promotion;

import com.enigma.x_food.feature.promotion.dto.request.NewPromotionRequest;
import com.enigma.x_food.feature.promotion.dto.response.PromotionResponse;
import com.enigma.x_food.shared.CommonResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.sql.Timestamp;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class PromotionControllerTest {
    @MockBean
    private PromotionService promotionService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Test
    void createNewPromotion() throws Exception {
        NewPromotionRequest promotion = NewPromotionRequest.builder()
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
        Mockito.when(promotionService.createNew(promotion)).thenReturn(promotionResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/promotions")
                        .content(objectMapper.writeValueAsString(promotion))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(result -> {
                    CommonResponse<PromotionResponse> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {}
                    );
                    Assertions.assertEquals(201, response.getStatusCode());
                    Assertions.assertEquals("adminID",response.getData().getAdminID());
                });
    }
}