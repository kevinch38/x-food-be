package com.enigma.x_food.feature.item.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewItemRequest {
    @NotBlank(message = "Item name cannot be empty")
    private String itemName;
    @NotBlank(message = "Category ID cannot be empty")
    private String categoryID;
    @NotBlank(message = "Branch ID cannot be empty")
    private String branchID;
    private MultipartFile image;
    @NotNull(message = "Initial price cannot be empty")
    private Double initialPrice;
    @NotNull(message = "Discounted price cannot be empty")
    private Double discountedPrice;
    @NotNull(message = "Item stock cannot be empty")
    private Integer itemStock;
    @NotNull(message = "Is discounted cannot be empty")
    private Boolean isDiscounted;
    @NotNull(message = "Is recommended cannot be empty")
    private Boolean isRecommended;
    @NotBlank(message = "Item description cannot be empty")
    private String itemDescription;
}
