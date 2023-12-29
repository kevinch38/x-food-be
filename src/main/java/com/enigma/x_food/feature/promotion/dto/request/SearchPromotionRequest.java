package com.enigma.x_food.feature.promotion.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchPromotionRequest {
    private Integer page;
    private Integer size;
    private String direction;
    private String sortBy;
    private String merchantID;
    private String promotionStatus;
    private LocalDate startCreatedAt;
    private LocalDate endCreatedAt;
    private LocalDate startUpdatedAt;
    private LocalDate endUpdatedAt;
    private LocalDate startExpiredDate;
    private LocalDate endExpiredDate;
}
