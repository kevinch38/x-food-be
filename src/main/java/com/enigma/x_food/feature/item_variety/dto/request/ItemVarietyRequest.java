package com.enigma.x_food.feature.item_variety.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemVarietyRequest {
    @NotBlank(message = "Item id cannot be empty")
    private String itemID;
    @NotNull(message = "Variety id cannot be empty")
    private String varietyID;
}
