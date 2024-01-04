package com.enigma.x_food.feature.order_item_sub_variety;

import com.enigma.x_food.feature.order_item_sub_variety.dto.request.OrderItemSubVarietyRequest;
import com.enigma.x_food.feature.order_item_sub_variety.dto.response.OrderItemSubVarietyResponse;

import java.util.List;

public interface OrderItemSubVarietyService {
    OrderItemSubVarietyResponse createNew(OrderItemSubVarietyRequest request);
    OrderItemSubVarietyResponse getById(String id);
    OrderItemSubVariety findById(String id);
    List<OrderItemSubVariety> getAll();
}
