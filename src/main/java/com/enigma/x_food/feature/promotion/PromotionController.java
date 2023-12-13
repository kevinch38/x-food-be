package com.enigma.x_food.feature.promotion;

import com.enigma.x_food.feature.promotion.dto.request.NewPromotionRequest;
import com.enigma.x_food.feature.promotion.dto.response.PromotionResponse;
import com.enigma.x_food.feature.promotion.dto.request.SearchPromotionRequest;
import com.enigma.x_food.shared.CommonResponse;
import com.enigma.x_food.shared.PagingResponse;
import com.enigma.x_food.util.PagingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<?> getAllPromotion(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(required = false, defaultValue = "accountID") String sortBy,
            @RequestParam(required = false) String merchantId,
            @RequestParam(required = false) String promotionDescription,
            @RequestParam(required = false) String promotionName,
            @RequestParam(required = false) String adminID,
            @RequestParam(required = false) String promotionStatusID,
            @RequestParam(required = false) String notes
    ) {
        page = PagingUtil.validatePage(page);
        size = PagingUtil.validateSize(size);
        direction = PagingUtil.validateDirection(direction);

        SearchPromotionRequest request = SearchPromotionRequest.builder()
                .page(page)
                .size(size)
                .direction(direction)
                .sortBy(sortBy)
                .merchantId(merchantId)
                .promotionDescription(promotionDescription)
                .promotionName(promotionName)
                .adminID(adminID)
                .promotionStatusID(promotionStatusID)
                .note(notes)
                .build();
        Page<PromotionResponse> promotions = promotionService.getAll(request);

        PagingResponse pagingResponse = PagingResponse.builder()
                .page(page)
                .size(size)
                .count(promotions.getTotalElements())
                .totalPages(promotions.getTotalPages())
                .build();

        CommonResponse<List<PromotionResponse>> response = CommonResponse.<List<PromotionResponse>>builder()
                .message("successfully get all promotion")
                .statusCode(HttpStatus.OK.value())
                .data(promotions.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
