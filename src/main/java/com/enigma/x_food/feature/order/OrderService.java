package com.enigma.x_food.feature.order;

import com.enigma.x_food.feature.order.dto.request.NewOrderRequest;
import com.enigma.x_food.feature.order.dto.request.SearchOrderRequest;
import com.enigma.x_food.feature.order.dto.request.UpdateOrderRequest;
import com.enigma.x_food.feature.order.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse createNew(NewOrderRequest request);
    List<OrderResponse> findByAccountId(SearchOrderRequest request);
    Order getById(String id);
    OrderResponse findById(String id);
    OrderResponse complete(UpdateOrderRequest request);
}
