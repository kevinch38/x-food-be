package com.enigma.x_food.feature.order_item;

import com.enigma.x_food.feature.item.Item;
import com.enigma.x_food.feature.item.ItemService;
import com.enigma.x_food.feature.order_item.dto.request.OrderItemRequest;
import com.enigma.x_food.feature.order_item.dto.request.SearchOrderItemRequest;
import com.enigma.x_food.feature.order_item.dto.response.OrderItemResponse;
import com.enigma.x_food.feature.sub_variety.SubVariety;
import com.enigma.x_food.feature.sub_variety.SubVarietyService;
import com.enigma.x_food.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final ItemService itemService;
    private final SubVarietyService subVarietyService;
    private final ValidationUtil validationUtil;

    @Override
    @Transactional(readOnly = true)
    public OrderItem findById(String id) {
        validationUtil.validate(id);
        return findByIdOrThrowException(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderItemResponse> getAll(SearchOrderItemRequest request) {
        validationUtil.validate(request);

        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());

        Specification<OrderItem> specification = getOrderItemSpecification(request);
        List<OrderItem> orderItems = orderItemRepository.findAll(specification, sort);
        return orderItems.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public OrderItem createNew(OrderItemRequest request) {
        validationUtil.validate(request);
        SubVariety subVariety = subVarietyService.getById(request.getSubVarietyID());
        Item item = itemService.findById(request.getItemID());

        OrderItem orderItem = OrderItem.builder()
                .item(item)
                .quantity(request.getQuantity())
                .subVariety(subVariety)
                .build();
        orderItemRepository.saveAndFlush(orderItem);

        log.info("End createNew");
        return orderItem;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String id) {
        validationUtil.validate(id);
        OrderItem orderItem = findByIdOrThrowException(id);
        orderItemRepository.delete(orderItem);
    }

    private OrderItem findByIdOrThrowException(String id) {
        return orderItemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "OrderItem not found"));
    }

    private OrderItemResponse mapToResponse(OrderItem orderItem) {
        return OrderItemResponse.builder()
                .orderItemID(orderItem.getOrderItemID())
                .orderID(orderItem.getOrder().getOrderID())
                .itemID(orderItem.getItem().getItemID())
                .subVarietyID(orderItem.getSubVariety().getSubVarietyID())
                .build();
    }

    private Specification<OrderItem> getOrderItemSpecification(SearchOrderItemRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getBranchID() != null) {
                Predicate predicate = criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("merchantBranch").get("branchID")),
                        request.getBranchID().toLowerCase()
                );
                predicates.add(predicate);
            }

            return query
                    .where(predicates.toArray(new Predicate[]{}))
                    .getRestriction();
        };
    }
}
