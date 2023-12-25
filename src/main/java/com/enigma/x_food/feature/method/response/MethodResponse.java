package com.enigma.x_food.feature.method.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MethodResponse {
    private String methodID;
    private String methodName;
}
