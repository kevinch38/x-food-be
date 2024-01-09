package com.enigma.x_food.feature.promotion;

import com.enigma.x_food.feature.promotion.dto.request.NewPromotionRequest;
import com.enigma.x_food.feature.promotion.dto.request.SearchActivePromotionRequest;
import com.enigma.x_food.feature.promotion.dto.request.SearchPromotionRequest;
import com.enigma.x_food.feature.promotion.dto.request.UpdatePromotionRequest;
import com.enigma.x_food.feature.promotion.dto.response.PromotionResponse;
import com.enigma.x_food.shared.CommonResponse;
import com.enigma.x_food.shared.PagingResponse;
import com.enigma.x_food.util.PagingUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/promotions")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class PromotionController {
    private final PromotionService promotionService;

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MARKETING_STAFF')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNew(@RequestBody NewPromotionRequest request) throws AuthenticationException {
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

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MARKETING_STAFF', 'PARTNERSHIP_STAFF', 'MARKETING_HEAD')")
    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(required = false, defaultValue = "promotionID") String sortBy,
            @RequestParam(required = false) String merchantID,
            @RequestParam(required = false) String promotionStatus,
            @RequestParam(required = false) String promotionName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startCreatedAt,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endCreatedAt,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startUpdatedAt,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endUpdatedAt,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startExpiredDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endExpiredDate
    ) {
        page = PagingUtil.validatePage(page);
        size = PagingUtil.validateSize(size);
        direction = PagingUtil.validateDirection(direction);

        SearchPromotionRequest request = SearchPromotionRequest.builder()
                .page(page)
                .size(size)
                .direction(direction)
                .sortBy(sortBy)
                .merchantID(merchantID)
                .promotionStatus(promotionStatus)
                .promotionName(promotionName)
                .startCreatedAt(startCreatedAt)
                .endCreatedAt(endCreatedAt)
                .startUpdatedAt(startUpdatedAt)
                .endUpdatedAt(endUpdatedAt)
                .startExpiredDate(startExpiredDate)
                .endExpiredDate(endExpiredDate)
                .build();

        Page<PromotionResponse> promotions = promotionService.getAll(request);

        PagingResponse pagingResponse = PagingResponse.builder()
                .page(page)
                .size(size)
                .count(promotions.getTotalElements())
                .totalPages(promotions.getTotalPages())
                .build();

        CommonResponse<List<PromotionResponse>> response = CommonResponse.<List<PromotionResponse>>builder()
                .message("successfully get all promotions")
                .statusCode(HttpStatus.OK.value())
                .data(promotions.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PreAuthorize("permitAll")
    @GetMapping("active")
    public ResponseEntity<?> getAllActive(
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(required = false, defaultValue = "promotionID") String sortBy,
            @RequestParam(required = false) String merchantID
    ) {
        direction = PagingUtil.validateDirection(direction);

        SearchActivePromotionRequest request = SearchActivePromotionRequest.builder()
                .direction(direction)
                .sortBy(sortBy)
                .merchantID(merchantID)
                .build();
        List<PromotionResponse> promotions = promotionService.getAllActive(request);

        CommonResponse<List<PromotionResponse>> response = CommonResponse.<List<PromotionResponse>>builder()
                .message("successfully get all active promotions")
                .statusCode(HttpStatus.OK.value())
                .data(promotions)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'PARTNERSHIP_STAFF', 'MARKETING_HEAD')")
    @PutMapping
    public ResponseEntity<?> update(@RequestBody UpdatePromotionRequest request) throws AuthenticationException {
        PromotionResponse promotionResponse = promotionService.update(request);
        CommonResponse<PromotionResponse> response = CommonResponse.<PromotionResponse>builder()
                .message("successfully update promotion")
                .statusCode(HttpStatus.OK.value())
                .data(promotionResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MARKETING_HEAD')")
    @PutMapping("/approve/active/{id}")
    public ResponseEntity<?> approveToActive(@PathVariable String id)  {
        promotionService.approveToActive(id);

        CommonResponse<String> response = CommonResponse.<String>builder()
                .message("successfully update merchant to active")
                .statusCode(HttpStatus.OK.value())
                .data("OK")
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MARKETING_HEAD')")
    @PutMapping("/approve/inactive/{id}")
    public ResponseEntity<?> approveToInactive(@PathVariable String id)  {
        promotionService.deleteApprove(id);

        CommonResponse<String> response = CommonResponse.<String>builder()
                .message("successfully update merchant to inactive")
                .statusCode(HttpStatus.OK.value())
                .data("OK")
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MARKETING_HEAD')")
    @PutMapping("/reject/update/{id}")
    public ResponseEntity<?> rejectUpdate(@PathVariable String id)  {
        promotionService.rejectUpdate(id);

        CommonResponse<String> response = CommonResponse.<String>builder()
                .message("successfully update merchant to active")
                .statusCode(HttpStatus.OK.value())
                .data("OK")
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MARKETING_STAFF', 'MARKETING_HEAD')")
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {
        PromotionResponse promotionResponse = promotionService.findById(id);
        CommonResponse<PromotionResponse> response = CommonResponse.<PromotionResponse>builder()
                .message("successfully get promotion")
                .statusCode(HttpStatus.OK.value())
                .data(promotionResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'PARTNERSHIP_STAFF')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) throws AuthenticationException {
        promotionService.deleteById(id);
        CommonResponse<?> response = CommonResponse.builder()
                .message("successfully delete promotion")
                .statusCode(HttpStatus.OK.value())
                .data("OK")
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
