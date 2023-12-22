package com.enigma.x_food.feature.order;

import com.enigma.x_food.feature.order.dto.request.OrderRequest;
import com.enigma.x_food.feature.order.dto.request.SearchOrderRequest;
import com.enigma.x_food.feature.order.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse createNew(OrderRequest request);
    List<OrderResponse> findByAccountId(SearchOrderRequest request);
    Order findById(String id);
}
