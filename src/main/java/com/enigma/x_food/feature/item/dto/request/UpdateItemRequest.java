package com.enigma.x_food.feature.item.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateItemRequest {
    @NotBlank(message = "Item ID cannot be empty")
    private String itemID;
    @NotBlank(message = "Item name cannot be empty")
    private String itemName;
    @NotBlank(message = "Category ID cannot be empty")
    private String categoryID;
    @NotBlank(message = "Branch ID cannot be empty")
    private String branchID;
    @NotBlank(message = "Image cannot be empty")
    private byte[] image;
    @NotBlank(message = "Initial price cannot be empty")
    private Double InitialPrice;
    @NotBlank(message = "Discounted price cannot be empty")
    private Double discountedPrice;
    @NotBlank(message = "Item stock cannot be empty")
    private Integer itemStock;
    @NotBlank(message = "Is discounted cannot be empty")
    private Boolean isDiscounted;
    @NotBlank(message = "Is recommended cannot be empty")
    private Boolean isRecommended;
    @NotBlank(message = "Item description cannot be empty")
    private String itemDescription;
}
