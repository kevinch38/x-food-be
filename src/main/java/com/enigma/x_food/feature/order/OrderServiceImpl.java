package com.enigma.x_food.feature.order;

import com.enigma.x_food.feature.history.History;
import com.enigma.x_food.feature.history.HistoryService;
import com.enigma.x_food.feature.history.dto.request.HistoryRequest;
import com.enigma.x_food.feature.merchant_branch.MerchantBranch;
import com.enigma.x_food.feature.merchant_branch.MerchantBranchService;
import com.enigma.x_food.feature.order.dto.request.OrderRequest;
import com.enigma.x_food.feature.order.dto.request.SearchOrderRequest;
import com.enigma.x_food.feature.order.dto.response.OrderResponse;
import com.enigma.x_food.feature.user.User;
import com.enigma.x_food.feature.user.UserService;
import com.enigma.x_food.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final HistoryService historyService;
    private final MerchantBranchService merchantBranchService;
    private final UserService userService;
    private final ValidationUtil validationUtil;
    private final EntityManager entityManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderResponse createNew(OrderRequest request) {
        try {
            log.info("Start createNew");
            validationUtil.validate(request);

            HistoryRequest historyRequest = HistoryRequest.builder()
                    .transactionType("ORDER")
                    .historyValue(request.getOrderValue())
                    .transactionDate(LocalDate.now())
                    .credit(false)
                    .debit(true)
                    .paymentID(null)
                    .topUpID(null)
                    .accountID(request.getAccountID())
                    .build();

            History history = historyService.createNew(historyRequest);
            User user = userService.getUserById(request.getAccountID());
            MerchantBranch merchantBranch = merchantBranchService.findById(request.getBranchID());

            Order order = Order.builder()
                    .user(entityManager.merge(user))
                    .history(entityManager.merge(history))
                    .orderValue(request.getOrderValue())
                    .notes(request.getNotes())
                    .tableNumber(request.getTableNumber())
                    .orderStatusID(request.getOrderStatusID())
                    .merchantBranch(entityManager.merge(merchantBranch))
                    .build();

            history.setOrder(order);

            orderRepository.saveAndFlush(order);
            log.info("End createNew");
            return mapToResponse(order);
        } catch (DataIntegrityViolationException e) {
            log.error("Error createNew: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "top up already exist");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> findByAccountId(SearchOrderRequest request) {
        Specification<Order> specification = getOrderSpecification(request);
        return orderRepository.findAll(specification).stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Order findById(String id) {
        return orderRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found")
                );
    }

    private OrderResponse mapToResponse(Order order) {
        return OrderResponse.builder()
                .accountID(order.getUser().getAccountID())
                .historyID(order.getHistory().getHistoryID())
                .orderValue(order.getOrderValue())
                .notes(order.getNotes())
                .tableNumber(order.getTableNumber())
                .orderStatusID(order.getOrderStatusID())
                .branchID(order.getMerchantBranch().getBranchID())
                .build();
    }

    private Specification<Order> getOrderSpecification(SearchOrderRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getAccountID() != null) {
                Join<Order, History> orderHistoryJoin = root.join("history", JoinType.INNER);

                Predicate predicate = criteriaBuilder.equal(
                        criteriaBuilder.lower(orderHistoryJoin.get("user").get("accountID")),
                        request.getAccountID().toLowerCase()
                );
                predicates.add(predicate);

                predicate = criteriaBuilder.equal(
                        criteriaBuilder.lower(orderHistoryJoin.get("transactionType")),
                        "topup"
                );
                predicates.add(predicate);
            }
            return query
                    .where(predicates.toArray(new Predicate[]{}))
                    .getRestriction();
        };
    }
}
