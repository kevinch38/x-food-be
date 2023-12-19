package com.enigma.x_food.feature.item.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemResponse {
    private String itemID;
    private String itemName;
    private String categoryID;
    private String branchID;
    private byte[] image;
    private Double initialPrice;
    private Double discountedPrice;
    private Integer itemStock;
    private Boolean isDiscounted;
    private Boolean isRecommended;
    private String itemDescription;
}
