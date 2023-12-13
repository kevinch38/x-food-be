package com.enigma.x_food.feature.merchant.dto.response;

import com.enigma.x_food.feature.pin.dto.response.PagingResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommonResponse<T> {
    private String message;
    private Integer statusCode;
    private T data;
    private PagingResponse paging;
}
