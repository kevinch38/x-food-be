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
    public OrderItemSplitResponse createNew(NewOrderItemSplitRequest request) {
        log.info("Start createNew");
        validationUtil.validate(request);

//        List<OrderItem> orderItems = new ArrayList<>();
//        for (OrderItemRequest orderItem : request.getOrderItems()) {
//            orderItems.add(OrderItem.builder()
//                    .orderItemID(orderItem.getItemID())
//
//                    .build());
//        }
        OrderItemSplit orderItemSplit = OrderItemSplit.builder().orderItems(request.getOrderItems()).build();
        orderItemSplitRepository.saveAndFlush(orderItemSplit);

        log.info("End createNew");
        return mapToResponse(orderItemSplit);
    }

    private OrderItemSplitResponse mapToResponse(OrderItemSplit order) {
        List<OrderItemResponse> orderItemResponses = order.getOrderItems().stream().map(
                        OrderItemSplitServiceImpl::getOrderItemResponse)
                .collect(Collectors.toList());

        return OrderItemSplitResponse.builder()
                .orderItemSplitID(order.getOrderItemSplitID())
                .orderItems(orderItemResponses)
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    private static OrderItemResponse getOrderItemResponse(OrderItem o) {
        List<OrderItemSubVarietyResponse> orderItemSubVarietyResponses = new ArrayList<>();
        if (o.getOrderItemSubVarieties() != null) {
            List<OrderItemSubVariety> orderItemSubVarieties = o.getOrderItemSubVarieties();
            orderItemSubVarietyResponses = orderItemSubVarieties.stream().map(
                            oisv -> OrderItemSubVarietyResponse.builder()
                                    .orderItemSubVarietyID(oisv.getOrderItemSubVarietyID())
                                    .subVariety(SubVarietyResponse.builder()
                                            .subVarietyID(oisv.getSubVariety().getSubVarietyID())
                                            .branchID(oisv.getSubVariety().getMerchantBranch().getBranchID())
                                            .subVarName(oisv.getSubVariety().getSubVarName())
                                            .subVarStock(oisv.getSubVariety().getSubVarStock())
                                            .subVarPrice(oisv.getSubVariety().getSubVarPrice())
                                            .build())
                                    .build()
                    )
                    .collect(Collectors.toList());
        }
        return OrderItemResponse.builder()
                .orderItemID(o.getOrderItemID())
                .itemName(o.getItem().getItemName())
                .orderItemSubVarieties(orderItemSubVarietyResponses)
                .price(o.getItem().getDiscountedPrice())
                .createdAt(o.getCreatedAt())
                .updatedAt(o.getUpdatedAt())
                .build();
    }

}
