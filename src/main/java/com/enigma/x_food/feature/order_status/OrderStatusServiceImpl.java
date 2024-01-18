package com.enigma.x_food.feature.order_status;

import com.enigma.x_food.constant.EOrderStatus;
import com.enigma.x_food.feature.order_status.response.OrderStatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderStatusServiceImpl implements OrderStatusService {
    private final OrderStatusRepository orderStatusRepository;

    @Transactional(rollbackFor = Exception.class)
    @PostConstruct
    public void init() {
        List<OrderStatus> orderStatus = new ArrayList<>();
        for (EOrderStatus value : EOrderStatus.values()) {
            Optional<OrderStatus> status = orderStatusRepository.findByStatus(value);
            if (status.isPresent()) continue;

            orderStatus.add(OrderStatus.builder()
                    .status(value)
                    .build());
        }
        orderStatusRepository.saveAllAndFlush(orderStatus);
    }

    @Transactional(readOnly = true)
    @Override
    public OrderStatus getByStatus(EOrderStatus status) {
        return findByStatusOrThrowNotFound(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderStatusResponse> getAll() {
        log.info("Start getAll");

        List<OrderStatus> orderStatus = orderStatusRepository.findAll();
        log.info("End getAll");
        return orderStatus.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private OrderStatus findByStatusOrThrowNotFound(EOrderStatus status) {
        return orderStatusRepository.findByStatus(status)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "order status not found"));
    }

    private OrderStatusResponse mapToResponse(OrderStatus orderStatus) {
        return OrderStatusResponse.builder()
                .orderStatusID(orderStatus.getOrderStatusID())
                .status(orderStatus.getStatus().toString())
                .createdAt(orderStatus.getCreatedAt())
                .updatedAt(orderStatus.getUpdatedAt())
                .build();
    }
}
