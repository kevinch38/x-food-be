package com.enigma.x_food.feature.order_item_split.order;
import com.enigma.x_food.feature.order_item.OrderItem;
import com.enigma.x_food.feature.order_item.dto.response.OrderItemResponse;
import com.enigma.x_food.feature.order_item_split.order.dto.request.NewOrderItemSplitRequest;
import com.enigma.x_food.feature.order_item_split.order.dto.response.OrderItemSplitResponse;
import com.enigma.x_food.feature.order_item_sub_variety.OrderItemSubVariety;
import com.enigma.x_food.feature.order_item_sub_variety.dto.response.OrderItemSubVarietyResponse;
import com.enigma.x_food.feature.sub_variety.dto.response.SubVarietyResponse;
import com.enigma.x_food.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderItemSplitServiceImpl implements OrderItemSplitService {
    private final OrderItemSplitRepository orderItemSplitRepository;
    private final ValidationUtil validationUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<OrderItemSplit> createNew(List<NewOrderItemSplitRequest> request) {
        log.info("Start createNew");
        validationUtil.validate(request);

        List<OrderItemSplit> orderItemSplits = new ArrayList<>();
        for (NewOrderItemSplitRequest newOrderItemSplitRequest : request) {
            OrderItemSplit orderItemSplit = OrderItemSplit.builder()
                    .orderItemID(newOrderItemSplitRequest.getOrderItemID())
                    .build();
            orderItemSplits.add(orderItemSplit);
        }
        orderItemSplitRepository.saveAllAndFlush(orderItemSplits);

        log.info("End createNew");
        return mapToResponse(orderItemSplits);
    }

    private List<OrderItemSplit> mapToResponse(List<OrderItemSplit> order) {
        List<OrderItemSplit> orderItemSplitResponses = new ArrayList<>();
        for (OrderItemSplit orderItemSplit : order) {
            OrderItemSplit build = OrderItemSplit.builder()
                    .orderItemSplitID(orderItemSplit.getOrderItemSplitID())
                    .orderItemID(orderItemSplit.getOrderItemID())
                    .createdAt(orderItemSplit.getCreatedAt())
                    .updatedAt(orderItemSplit.getUpdatedAt())
                    .build();
            orderItemSplitResponses.add(build);
        }
        return orderItemSplitResponses;
    }

}
