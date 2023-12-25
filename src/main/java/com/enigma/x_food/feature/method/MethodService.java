package com.enigma.x_food.feature.method;

import com.enigma.x_food.constant.EMethod;
import com.enigma.x_food.feature.method.response.MethodResponse;

import java.util.List;

public interface MethodService {
    Method getByMethodName(EMethod name);
    List<MethodResponse> getAll();
}
