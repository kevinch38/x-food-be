package com.enigma.x_food.feature.sub_variety.dto.request;

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
public class SubVarietyRequest {
    @NotBlank(message = "Branch Id cannot be empty")
    private String branchID;
    @NotBlank(message = "Sub variety name cannot be empty")
    private String subVarName;
    @NotNull(message = "Sub variety stock cannot be empty")
    private Integer subVarStock;
    @NotNull(message = "Sub variety price cannot be empty")
    private Double subVarPrice;
}
