package com.enigma.x_food.feature.promotion;

import com.enigma.x_food.feature.promotion.dto.request.NewPromotionRequest;
import com.enigma.x_food.feature.promotion.dto.request.SearchPromotionRequest;
import com.enigma.x_food.feature.promotion.dto.request.UpdatePromotionRequest;
import com.enigma.x_food.feature.promotion.dto.response.PromotionResponse;
import com.enigma.x_food.shared.CommonResponse;
import com.enigma.x_food.util.ValidationUtil;
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
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

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
    @Autowired
    private ValidationUtil validationUtil;

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
                .build();
        PromotionResponse promotionResponse = PromotionResponse.builder()
                .promotionID("1")
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
                .build();
        Mockito.when(promotionService.createNew(promotion)).thenReturn(promotionResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/promotions")
                        .content(objectMapper.writeValueAsString(promotion))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(result -> {
                    CommonResponse<PromotionResponse> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            }
                    );
                    Assertions.assertEquals(201, response.getStatusCode());
                    Assertions.assertEquals("adminID", response.getData().getAdminID());
                });
    }

    @Test
    void getAllPromotion() throws Exception {
        SearchPromotionRequest searchPromotionRequest = SearchPromotionRequest.builder()
                .page(1)
                .size(10)
                .direction("asc")
                .sortBy("accountID")
                .build();
        Sort.Direction direction = Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(
                searchPromotionRequest.getPage(),
                searchPromotionRequest.getSize(),
                direction,
                searchPromotionRequest.getSortBy());
        PromotionResponse promotionResponse = PromotionResponse.builder()
                .promotionID("1")
                .merchantID("1")
                .cost(1d)
                .maxRedeem(1)
                .promotionValue(2)
                .promotionDescription("tes")
                .promotionName("promotion tes")
                .quantity(3)
                .adminID("1")
                .expiredDate(new Timestamp(System.currentTimeMillis()))
                .promotionStatusID("1")
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .notes("note")
                .build();
        List<PromotionResponse> promotionResponses = List.of(promotionResponse);
        Page<PromotionResponse> pagePromotionResponses = new PageImpl<>(promotionResponses, pageable, 0);

        Mockito.when(promotionService.getAll(Mockito.any())).thenReturn(pagePromotionResponses);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/promotions")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(result -> {
                    CommonResponse<List<PromotionResponse>> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {}
                    );
                    Assertions.assertEquals(200, response.getStatusCode());
                    Assertions.assertEquals("1",response.getData().get(0).getPromotionID());
                });
    }

    @Test
    void updatePromotion() throws Exception {
        UpdatePromotionRequest request = UpdatePromotionRequest.builder()
                .promotionID("1")
                .merchantID("1")
                .cost(1d)
                .maxRedeem(1)
                .promotionValue(2)
                .promotionDescription("tes")
                .promotionName("promotion tes")
                .quantity(3)
                .expiredDate(new Timestamp(System.currentTimeMillis()))
                .promotionStatusID("1")
                .notes("note")
                .build();

        PromotionResponse promotionResponse = PromotionResponse.builder()
                .promotionID("1")
                .merchantID("1")
                .cost(1d)
                .maxRedeem(1)
                .promotionValue(2)
                .promotionDescription("tes")
                .promotionName("promotion tes")
                .quantity(3)
                .adminID("1")
                .expiredDate(new Timestamp(System.currentTimeMillis()))
                .promotionStatusID("1")
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .notes("note")
                .build();

        when(promotionService.update(request))
                .thenReturn(promotionResponse);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/api/promotions")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(result -> {
                    CommonResponse<PromotionResponse> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            }
                    );

                    assertEquals(200, result.getResponse().getStatus());
                    assertEquals(3, response.getData().getQuantity());
                    assertNotNull(response.getData());
                });
    }

    @Test
    void deletePromotionById() throws Exception {
        String id = "1";

        doNothing().when(promotionService).deleteById(id);

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/promotions/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(result -> {
                    CommonResponse<String> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            }
                    );
                    assertEquals(200, result.getResponse().getStatus());
                    assertNotNull(response);
                    assertEquals("OK", response.getData());
                    verify(promotionService).deleteById(id);
                });
    }
}