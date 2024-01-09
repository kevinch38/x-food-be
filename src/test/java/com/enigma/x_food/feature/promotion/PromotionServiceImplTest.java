package com.enigma.x_food.feature.promotion;

import com.enigma.x_food.feature.merchant.Merchant;
import com.enigma.x_food.feature.merchant.MerchantService;
import com.enigma.x_food.feature.merchant.dto.response.MerchantResponse;
import com.enigma.x_food.feature.promotion.dto.request.NewPromotionRequest;
import com.enigma.x_food.feature.promotion.dto.request.SearchPromotionRequest;
import com.enigma.x_food.feature.promotion.dto.request.UpdatePromotionRequest;
import com.enigma.x_food.feature.promotion.dto.response.PromotionResponse;
import com.enigma.x_food.util.ValidationUtil;
import org.apache.tomcat.websocket.AuthenticationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PromotionServiceImplTest {
    @Autowired
    private PromotionService promotionService;
    @MockBean
    private MerchantService merchantService;
    @MockBean
    private EntityManager entityManager;
    @MockBean
    private PromotionRepository promotionRepository;
    @MockBean
    private ValidationUtil validationUtil;

    @Test
    void createNew() throws AuthenticationException {
        String id ="1";
        NewPromotionRequest newPromotionRequest = NewPromotionRequest.builder()
                .merchantID(id)
                .cost(100)
                .maxRedeem(3)
                .promotionValue(200)
                .promotionDescription("tes")
                .promotionName("promotion tes")
                .quantity(2)
                .expiredDate(new Timestamp(System.currentTimeMillis()))
                .build();

        MerchantResponse merchantResponse = MerchantResponse.builder()
                .merchantID(id)
                .joinDate(new Timestamp(System.currentTimeMillis()))
                .merchantName("Burger")
                .picName("name")
                .picNumber("num")
                .picEmail("email")
                .merchantDescription("desc")
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .notes("note")
                .build();

        Merchant merchant = Merchant.builder()
                .merchantID(merchantResponse.getMerchantID())
                .joinDate(merchantResponse.getJoinDate())
                .merchantName(merchantResponse.getMerchantName())
                .picName(merchantResponse.getPicName())
                .picNumber(merchantResponse.getPicNumber())
                .picEmail(merchantResponse.getPicEmail())
                .merchantDescription(merchantResponse.getMerchantDescription())
                .createdAt(merchantResponse.getCreatedAt())
                .updatedAt(merchantResponse.getUpdatedAt())
                .notes(merchantResponse.getNotes())
                .build();

        Promotion promotion = Promotion.builder()
                .merchant(merchant)
                .cost(newPromotionRequest.getCost())
                .maxRedeem(newPromotionRequest.getMaxRedeem())
                .promotionValue(newPromotionRequest.getPromotionValue())
                .promotionDescription(newPromotionRequest.getPromotionDescription())
                .promotionName(newPromotionRequest.getPromotionName())
                .quantity(newPromotionRequest.getQuantity())
                .expiredDate(newPromotionRequest.getExpiredDate())
                .build();

        Mockito.doNothing().when(validationUtil).validate(newPromotionRequest);
        when(merchantService.findById(id)).thenReturn(merchantResponse);
        when(promotionRepository.saveAndFlush(promotion))
                .thenReturn(promotion);
        when(entityManager.merge(any())).thenReturn(merchant);
        PromotionResponse actual = promotionService.createNew(newPromotionRequest);

    }

    @Test
    void update() throws AuthenticationException {
        String id = "1";
        MerchantResponse merchantResponse = MerchantResponse.builder()
                .merchantID("1")
                .joinDate(new Timestamp(System.currentTimeMillis()))
                .merchantName("Burger")
                .picName("name")
                .picNumber("num")
                .picEmail("email")
                .merchantDescription("desc")
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .notes("note")
                .build();

        Merchant merchant = Merchant.builder()
                .merchantID(merchantResponse.getMerchantID())
                .joinDate(merchantResponse.getJoinDate())
                .merchantName(merchantResponse.getMerchantName())
                .picName(merchantResponse.getPicName())
                .picNumber(merchantResponse.getPicNumber())
                .picEmail(merchantResponse.getPicEmail())
                .merchantDescription(merchantResponse.getMerchantDescription())
                .createdAt(merchantResponse.getCreatedAt())
                .updatedAt(merchantResponse.getUpdatedAt())
                .notes(merchantResponse.getNotes())
                .build();

        UpdatePromotionRequest request = UpdatePromotionRequest.builder()
                .promotionID("1")
                .build();

        Promotion promotion = Promotion.builder()
                .promotionID(id)
                .merchant(Merchant.builder().merchantID("1").build())
                .maxRedeem(1)
                .promotionValue(2)
                .promotionDescription("tes")
                .promotionName("promotion tes")
                .quantity(3)
                .expiredDate(new Timestamp(System.currentTimeMillis()))
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .notes("note")
                .build();

        doNothing().when(validationUtil).validate(request);
        when(merchantService.findById(id)).thenReturn(merchantResponse);
        when(promotionRepository.findById(id)).thenReturn(Optional.of(promotion));
        when(entityManager.merge(any())).thenReturn(merchant);
        when(promotionRepository.saveAndFlush(any()))
                .thenReturn(promotion);

        PromotionResponse promotionResponse = promotionService.update(request);

        verify(validationUtil).validate(request);
        assertNotNull(promotionResponse);
        assertEquals("1", promotionResponse.getPromotionID());
    }

    @Test
    void getAll() {
        SearchPromotionRequest searchPromotionRequest = SearchPromotionRequest.builder()
                .direction("asc")
                .page(1)
                .size(10)
                .sortBy("accountID")
                .build();

        Promotion promotion = Promotion.builder()
                .promotionID("1")
                .merchant(Merchant.builder().merchantID("1").build())
                .cost(1)
                .maxRedeem(1)
                .promotionValue(2)
                .promotionDescription("tes")
                .promotionName("promotion tes")
                .quantity(3)
                .expiredDate(new Timestamp(System.currentTimeMillis()))
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .notes("note")
                .build();
        Page<Promotion> promotionPage = new PageImpl<>(List.of(promotion));
        Mockito.when(promotionRepository.findAll(Mockito.isA(Specification.class),Mockito.isA(Pageable.class))).thenReturn(promotionPage);

        Page<PromotionResponse> actual = promotionService.getAll(searchPromotionRequest);

        assertEquals(1,actual.getTotalElements());
    }

    @Test
    void deleteById() throws AuthenticationException {
        String id = " 1";
        Promotion promotion = Promotion.builder()
                .promotionID("1")
                .merchant(Merchant.builder().merchantID("1").build())
                .cost(1)
                .maxRedeem(1)
                .promotionValue(2)
                .promotionDescription("tes")
                .promotionName("promotion tes")
                .quantity(3)
                .expiredDate(new Timestamp(System.currentTimeMillis()))
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .notes("note")
                .build();

        when(promotionRepository.findById(id)).thenReturn(Optional.of(promotion));
        doNothing().when(promotionRepository).deleteById(any());

        promotionService.deleteById(id);

        verify(promotionRepository).delete(promotion);
    }
}