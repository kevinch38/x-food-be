package com.enigma.x_food.feature.promotion;

import com.enigma.x_food.feature.promotion.dto.request.NewPromotionRequest;
import com.enigma.x_food.feature.promotion.dto.response.PromotionResponse;
import com.enigma.x_food.shared.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/promotions")
@RequiredArgsConstructor
public class PromotionController {
    private final PromotionService promotionService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNewPromotion(@RequestBody NewPromotionRequest request) {
        PromotionResponse promotionResponse = promotionService.createNew(request);
        CommonResponse<PromotionResponse> response = CommonResponse.<PromotionResponse>builder()
                .message("successfully create new promotion")
                .statusCode(HttpStatus.CREATED.value())
                .data(promotionResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
