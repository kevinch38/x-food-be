package com.enigma.x_food.feature.item.dto.response;

import com.enigma.x_food.feature.item_variety.dto.response.ItemVarietyResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemResponse {
    private String itemID;
    private String itemName;
    private String category;
    private String branchID;
    private byte[] image;
    private Double initialPrice;
    private Double discountedPrice;
    private Integer itemStock;
    private Boolean isDiscounted;
    private Boolean isRecommended;
    private String itemDescription;
    private List<ItemVarietyResponse> itemVarieties;
}
