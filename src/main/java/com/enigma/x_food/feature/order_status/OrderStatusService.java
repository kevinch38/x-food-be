package com.enigma.x_food.feature.order_status;

import com.enigma.x_food.constant.EOrderStatus;
import com.enigma.x_food.feature.order_status.response.OrderStatusResponse;

import java.util.List;

public interface OrderStatusService {
    OrderStatus getByStatus(EOrderStatus status);
    List<OrderStatusResponse> getAll();
}
