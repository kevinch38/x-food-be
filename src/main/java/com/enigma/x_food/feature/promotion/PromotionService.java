package com.enigma.x_food.feature.promotion;

import com.enigma.x_food.feature.promotion.dto.request.*;
import com.enigma.x_food.feature.promotion.dto.response.PromotionResponse;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PromotionService {
    PromotionResponse createNew(NewPromotionRequest request) throws AuthenticationException;
    PromotionResponse update(UpdatePromotionRequest request) throws AuthenticationException;
    void deleteById(String id) throws AuthenticationException;
    PromotionResponse findById(String id);
    Promotion getPromotionById(String id);
    Page<PromotionResponse> getAll(SearchPromotionRequest request);
    List<PromotionResponse> getAllActive(SearchActivePromotionRequest request);
    void approveToActive(ApprovalPromotionRequest request) throws AuthenticationException;
    void deleteApprove(ApprovalPromotionRequest request) throws AuthenticationException;
    void rejectUpdate(ApprovalPromotionRequest request) throws AuthenticationException;
}
