package com.enigma.x_food.feature.promotion.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchPromotionRequest {
    private Integer page;
    private Integer size;
    private String direction;
    private String sortBy;
    private String merchantId;
    private String promotionDescription;
    private String promotionName;
    private String adminID;
    private String promotionStatusID;
    private String note;
}
