package com.enigma.x_food.feature.order_item_sub_variety;

import com.enigma.x_food.feature.order_item_sub_variety.dto.request.OrderItemSubVarietyRequest;
import com.enigma.x_food.feature.order_item_sub_variety.dto.response.OrderItemSubVarietyResponse;
import com.enigma.x_food.feature.sub_variety.SubVariety;
import com.enigma.x_food.feature.sub_variety.dto.response.SubVarietyResponse;
import com.enigma.x_food.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderItemSubVarietyServiceImpl implements OrderItemSubVarietyService {
    private final OrderItemSubVarietyRepository orderItemSubVarietyRepository;
    private final ValidationUtil validationUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderItemSubVariety createNew(OrderItemSubVarietyRequest request) {
        validationUtil.validate(request);

        OrderItemSubVariety varietySubVariety = OrderItemSubVariety.builder()
                .subVariety(request.getSubVariety())
                .orderItem(request.getOrderItem())
                .build();

        return orderItemSubVarietyRepository.saveAndFlush(varietySubVariety);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderItemSubVariety> getAll() {
        return orderItemSubVarietyRepository.findAll();
    }

    @Override
    public OrderItemSubVarietyResponse getById(String id) {
        return mapToResponse(findByIdOrThrowNotFound(id));
    }

    @Override
    public OrderItemSubVariety findById(String id) {
        return findByIdOrThrowNotFound(id);
    }

    private OrderItemSubVariety findByIdOrThrowNotFound(String id) {
        return orderItemSubVarietyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "variety not found"));
    }

    private OrderItemSubVarietyResponse mapToResponse(OrderItemSubVariety orderItemSubVariety) {
        SubVariety subVariety = orderItemSubVariety.getSubVariety();
        SubVarietyResponse subVarietyResponse = SubVarietyResponse.builder()
                .subVarietyID(subVariety.getSubVarietyID())
                .branchID(subVariety.getMerchantBranch().getBranchID())
                .subVarName(subVariety.getSubVarName())
                .subVarStock(subVariety.getSubVarStock())
                .subVarPrice(subVariety.getSubVarPrice())
                .build();

        return OrderItemSubVarietyResponse.builder()
                .orderItemSubVarietyID(orderItemSubVariety.getOrderItemSubVarietyID())
                .subVariety(subVarietyResponse)
                .build();
    }


}
