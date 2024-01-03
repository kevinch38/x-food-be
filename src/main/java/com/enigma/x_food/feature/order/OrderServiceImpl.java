package com.enigma.x_food.feature.order;

import com.enigma.x_food.constant.EOrderStatus;
import com.enigma.x_food.constant.EPaymentStatus;
import com.enigma.x_food.constant.EPaymentType;
import com.enigma.x_food.constant.ETransactionType;
import com.enigma.x_food.feature.history.History;
import com.enigma.x_food.feature.history.HistoryService;
import com.enigma.x_food.feature.history.dto.request.HistoryRequest;
import com.enigma.x_food.feature.item.Item;
import com.enigma.x_food.feature.item.ItemService;
import com.enigma.x_food.feature.merchant_branch.MerchantBranch;
import com.enigma.x_food.feature.merchant_branch.MerchantBranchService;
import com.enigma.x_food.feature.order.dto.request.NewOrderRequest;
import com.enigma.x_food.feature.order.dto.request.SearchOrderRequest;
import com.enigma.x_food.feature.order.dto.request.UpdateOrderRequest;
import com.enigma.x_food.feature.order.dto.response.OrderResponse;
import com.enigma.x_food.feature.order_item.OrderItem;
import com.enigma.x_food.feature.order_item.OrderItemService;
import com.enigma.x_food.feature.order_item.dto.request.OrderItemRequest;
import com.enigma.x_food.feature.order_status.OrderStatus;
import com.enigma.x_food.feature.order_status.OrderStatusService;
import com.enigma.x_food.feature.payment.Payment;
import com.enigma.x_food.feature.payment.PaymentService;
import com.enigma.x_food.feature.payment.dto.request.PaymentRequest;
import com.enigma.x_food.feature.payment_status.PaymentStatus;
import com.enigma.x_food.feature.payment_status.PaymentStatusService;
import com.enigma.x_food.feature.user.User;
import com.enigma.x_food.feature.user.UserService;
import com.enigma.x_food.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
    private final OrderStatusService orderStatusService;
    private final OrderItemService orderItemService;
    private final PaymentService paymentService;
    private final PaymentStatusService paymentStatusService;
    private final ItemService itemService;
    private final UserService userService;
    private final ValidationUtil validationUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderResponse createNew(NewOrderRequest request) {
        log.info("Start createNew");
        validationUtil.validate(request);

        double price = 0d;

        User user = userService.getUserById(request.getAccountID());
        MerchantBranch merchantBranch = merchantBranchService.getById(request.getBranchID());
        OrderStatus orderStatus = orderStatusService.getByStatus(EOrderStatus.WAITING_FOR_PAYMENT);
        Order order = Order.builder()
                .user(user)
                .notes(request.getNotes())
                .tableNumber(request.getTableNumber())
                .orderStatus(orderStatus)
                .merchantBranch(merchantBranch)
                .build();

        for (OrderItemRequest orderItem : request.getOrderItems()) {
            Item item = itemService.findById(orderItem.getItemID());
            price += item.getDiscountedPrice() * orderItem.getQuantity();

            OrderItemRequest orderItemRequest = OrderItemRequest.builder()
                    .itemID(orderItem.getItemID())
                    .quantity(orderItem.getQuantity())
                    .build();
            OrderItem newOrderItem = orderItemService.createNew(orderItemRequest);
            newOrderItem.setOrder(order);
        }

        HistoryRequest historyRequest = HistoryRequest.builder()
                .transactionType(ETransactionType.ORDER.name())
                .historyValue(price)
                .transactionDate(LocalDate.now())
                .credit(true)
                .debit(false)
                .accountID(request.getAccountID())
                .build();

        History history = historyService.createNew(historyRequest);

        order.setHistory(history);
        order.setOrderValue(price);
        history.setOrder(order);

        orderRepository.saveAndFlush(order);

        PaymentStatus paymentStatus = paymentStatusService.getByStatus(EPaymentStatus.PENDING);

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .user(user)
                .paymentAmount(price)
                .order(order)
                .history(history)
                .build();

        Payment payment = paymentService.createNew(paymentRequest);
        payment.setPaymentType(EPaymentType.ORDER.name());
        payment.setPaymentStatus(paymentStatus);

        log.info("End createNew");
        return mapToResponse(order);
    }

    @Override
    public OrderResponse complete(UpdateOrderRequest request) {
        validationUtil.validate(request);
        Order order = findById(request.getOrderID());
        User user = userService.getUserById(request.getAccountID());

        OrderStatus orderStatus = orderStatusService.getByStatus(EOrderStatus.DONE);
        order.setOrderStatus(orderStatus);
        for (OrderItem orderItem : order.getOrderItems()) {
            Item item = itemService.findById(order.getOrderID());
            item.setItemStock(item.getItemStock() - orderItem.getQuantity());
        }

        user.getLoyaltyPoint().setLoyaltyPointAmount((int) (order.getOrderValue() / 10000));
        if (user.getBalance().getTotalBalance() < order.getOrderValue())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient balance to place the order");

        user.getBalance().setTotalBalance(user.getBalance().getTotalBalance() - order.getOrderValue());

        return mapToResponse(orderRepository.saveAndFlush(order));
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
                .orderID(order.getOrderID())
                .accountID(order.getUser().getAccountID())
                .historyID(order.getHistory().getHistoryID())
                .orderValue(order.getOrderValue())
                .notes(order.getNotes())
                .tableNumber(order.getTableNumber())
                .orderStatus(order.getOrderStatus().getStatus().name())
                .branchID(order.getMerchantBranch().getBranchID())
                .image(order.getMerchantBranch().getImage())
                .items(order.getOrderItems().stream().mapToInt(OrderItem::getQuantity).sum())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    private Specification<Order> getOrderSpecification(SearchOrderRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getAccountID() != null) {
                Join<Order, History> orderHistoryJoin = root.join("history", JoinType.INNER);

                Predicate predicate = criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("user").get("accountID")),
                        request.getAccountID().toLowerCase()
                );
                predicates.add(predicate);

                predicate = criteriaBuilder.equal(
                        criteriaBuilder.lower(orderHistoryJoin.get("transactionType")),
                        "order"
                );
                predicates.add(predicate);
            }
            return query
                    .where(predicates.toArray(new Predicate[]{}))
                    .getRestriction();
        };
    }
}
