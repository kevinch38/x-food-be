package com.enigma.x_food.feature.order_item;

import com.enigma.x_food.feature.order_item.dto.request.OrderItemRequest;
import com.enigma.x_food.feature.order_item.dto.request.SearchOrderItemRequest;
import com.enigma.x_food.feature.order_item.dto.response.OrderItemResponse;

import java.util.List;

public interface OrderItemService {
    void deleteById(String id);
    OrderItem findById(String id);
    List<OrderItemResponse> getAll(SearchOrderItemRequest request);
    OrderItem createNew(OrderItemRequest request);
}
