package com.enigma.x_food.feature.otp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchOTPRequest {
    private Integer page;
    private Integer size;
    private String direction;
    private String sortBy;
}
