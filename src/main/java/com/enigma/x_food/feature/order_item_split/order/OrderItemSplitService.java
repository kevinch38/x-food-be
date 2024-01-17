package com.enigma.x_food.feature.order_item_split.order;

import com.enigma.x_food.feature.order_item_split.order.dto.request.NewOrderItemSplitRequest;
import com.enigma.x_food.feature.order_item_split.order.dto.response.OrderItemSplitResponse;

import java.util.List;

public interface OrderItemSplitService {
    List<OrderItemSplit> createNew(List<NewOrderItemSplitRequest> request);
    OrderItemSplit getById(String id);
}
