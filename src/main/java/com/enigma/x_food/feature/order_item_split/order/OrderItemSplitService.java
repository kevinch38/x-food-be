package com.enigma.x_food.feature.order_item_split.order;

import com.enigma.x_food.feature.order_item_split.order.dto.request.NewOrderItemSplitRequest;
import com.enigma.x_food.feature.order_item_split.order.dto.response.OrderItemSplitResponse;

public interface OrderItemSplitService {
    OrderItemSplitResponse createNew(NewOrderItemSplitRequest request);
}
