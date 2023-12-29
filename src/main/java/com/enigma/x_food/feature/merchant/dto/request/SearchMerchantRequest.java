package com.enigma.x_food.feature.merchant.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchMerchantRequest {
    private Integer page;
    private Integer size;
    private String direction;
    private String sortBy;
    private String merchantName;
    private String merchantStatus;
    private LocalDate startCreatedAt;
    private LocalDate endCreatedAt;
    private LocalDate startUpdatedAt;
    private LocalDate endUpdatedAt;
    private LocalDate startExpiredDate;
    private LocalDate endExpiredDate;
    private LocalDate startJoinDate;
    private LocalDate endJoinDate;
}
