package com.enigma.x_food.feature.history;

import com.enigma.x_food.constant.ETransactionType;
import com.enigma.x_food.feature.friend.FriendService;
import com.enigma.x_food.feature.friend.dto.response.FriendResponse;
import com.enigma.x_food.feature.history.dto.request.HistoryRequest;
import com.enigma.x_food.feature.history.dto.request.SearchAccountHistoryRequest;
import com.enigma.x_food.feature.history.dto.request.SearchHistoryRequest;
import com.enigma.x_food.feature.history.dto.response.HistoryResponse;
import com.enigma.x_food.feature.order.Order;
import com.enigma.x_food.feature.order.dto.response.OrderResponse;
import com.enigma.x_food.feature.order_item.OrderItem;
import com.enigma.x_food.feature.order_item.dto.response.OrderItemResponse;
import com.enigma.x_food.feature.order_item_sub_variety.OrderItemSubVariety;
import com.enigma.x_food.feature.order_item_sub_variety.dto.response.OrderItemSubVarietyResponse;
import com.enigma.x_food.feature.payment.Payment;
import com.enigma.x_food.feature.payment.dto.response.PaymentResponse;
import com.enigma.x_food.feature.sub_variety.dto.response.SubVarietyResponse;
import com.enigma.x_food.feature.top_up.TopUp;
import com.enigma.x_food.feature.top_up.dto.response.TopUpResponse;
import com.enigma.x_food.feature.user.User;
import com.enigma.x_food.feature.user.UserService;
import com.enigma.x_food.util.SortingUtil;
import com.enigma.x_food.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
public class HistoryServiceImpl implements HistoryService {
    private final HistoryRepository historyRepository;
    private final UserService userService;
    private final FriendService friendService;
    private final ValidationUtil validationUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public History createNew(HistoryRequest request) {
        try {
            log.info("Start createNew");
            validationUtil.validate(request);

            User user = userService.getUserById(request.getAccountID());

            History history = History.builder()
                    .transactionType(ETransactionType.valueOf(request.getTransactionType()).name())
                    .historyValue(request.getHistoryValue())
                    .transactionDate(LocalDate.now())
                    .credit(request.getCredit())
                    .debit(request.getDebit())
                    .user(user)
                    .build();

            historyRepository.save(history);
            log.info("End createNew");
            return history;
        } catch (DataIntegrityViolationException e) {
            log.error("Error createNew: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "history already exist");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoryResponse> findByAccountId(SearchAccountHistoryRequest request) {
        validationUtil.validate(request);

        Specification<History> specification = getHistoryByAccountIDSpecification(request);
        List<History> histories = historyRepository.findAll(specification);
        return histories.stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public History findById(String id) {
        return historyRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "History not found")
        );
    }

    @Override
    public Page<HistoryResponse> findAll(SearchHistoryRequest request) {
        validationUtil.validate(request);

        String fieldName = SortingUtil.sortByValidation(User.class, request.getSortBy(), "historyID");
        request.setSortBy(fieldName);

        Sort.Direction direction = Sort.Direction.fromString(request.getDirection());
        Pageable pageable = PageRequest.of(
                request.getPage() - 1,
                request.getSize(),
                direction,
                request.getSortBy()
        );

        Specification<History> specification = getAllHistorySpecification(request);
        Page<History> histories = historyRepository.findAll(specification, pageable);
        return histories.map(this::mapToResponse);
    }

    private HistoryResponse mapToResponse(History history) {
        TopUp topUp = history.getTopUp();
        TopUpResponse topUpResponse = null;

        Order order = history.getOrder();
        OrderResponse orderResponse = null;

        Payment payment = history.getPayment();
        PaymentResponse paymentResponse = null;

        if (topUp != null) {
            topUpResponse = TopUpResponse.builder()
                    .topUpID(topUp.getTopUpID())
                    .topUpAmount(topUp.getTopUpAmount())
                    .method(topUp.getMethod().getMethodName().name())
                    .topUpFee(topUp.getTopUpFee())
                    .createdAt(topUp.getCreatedAt())
                    .updatedAt(topUp.getUpdatedAt())
                    .balanceID(topUp.getBalance().getBalanceID())
                    .historyID(topUp.getHistory().getHistoryID())
                    .build();
        } else if (order != null) {
            orderResponse = OrderResponse.builder()
                    .orderID(order.getOrderID())
                    .accountID(order.getUser().getAccountID())
                    .historyID(order.getHistory().getHistoryID())
                    .orderValue(order.getOrderValue())
                    .notes(order.getNotes())
                    .tableNumber(order.getTableNumber())
                    .orderStatus(order.getOrderStatus().getStatus().name())
                    .branchID(order.getMerchantBranch().getBranchID())
                    .merchantName(order.getMerchantBranch().getMerchant().getMerchantName())
                    .image(order.getMerchantBranch().getImage())
                    .quantity(order.getOrderItems().size())
                    .orderItems(order.getOrderItems().stream().map(
                            o -> getOrderItemResponse(order, o)
                    ).collect(Collectors.toList()))
                    .isSplit(order.getIsSplit())
                    .createdAt(order.getCreatedAt())
                    .updatedAt(order.getUpdatedAt())
                    .build();
        }
        if (payment != null) {
            FriendResponse friendResponse = null;
            if (payment.getFriend() != null)
                friendResponse = friendService.findById(payment.getFriend().getFriendID());

            paymentResponse = PaymentResponse.builder()
                    .paymentID(payment.getPaymentID())
                    .accountID(payment.getUser().getAccountID())
                    .paymentAmount(payment.getPaymentAmount())
                    .expiredAt(payment.getExpiredAt())
                    .paymentStatus(payment.getPaymentStatus().getStatus().name())
                    .paymentType(payment.getPaymentType())
                    .historyID(payment.getHistory().getHistoryID())
                    .friend(friendResponse)
                    .orderID(payment.getOrder().getOrderID())
                    .createdAt(payment.getCreatedAt())
                    .updatedAt(payment.getUpdatedAt())
                    .build();
        }
        return HistoryResponse.builder()
                .historyID(history.getHistoryID())
                .transactionType(history.getTransactionType())
                .historyValue(history.getHistoryValue())
                .transactionDate(LocalDate.now())
                .credit(history.getCredit())
                .debit(history.getDebit())
                .order(orderResponse)
                .payment(paymentResponse)
                .topUp(topUpResponse)
                .accountID(history.getUser().getAccountID())
                .createdAt(history.getCreatedAt())
                .updatedAt(history.getUpdatedAt())
                .build();
    }

    private static OrderItemResponse getOrderItemResponse(Order order, OrderItem o) {
        List<OrderItemSubVarietyResponse> orderItemSubVarietyResponses = new ArrayList<>();
        if (o.getOrderItemSubVarieties() == null) {
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
                .orderID(order.getOrderID())
                .itemName(o.getItem().getItemName())
                .orderItemSubVarieties(orderItemSubVarietyResponses)
                .price(o.getItem().getDiscountedPrice())
                .createdAt(o.getCreatedAt())
                .updatedAt(o.getUpdatedAt())
                .build();
    }

    private Specification<History> getHistoryByAccountIDSpecification(SearchAccountHistoryRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getAccountID() != null) {
                Join<User, History> userHistoryJoin = root.join("user", JoinType.INNER);

                Predicate predicate = criteriaBuilder.equal(
                        criteriaBuilder.lower(userHistoryJoin.get("accountID")),
                        request.getAccountID().toLowerCase()
                );
                predicates.add(predicate);
            }

            return query
                    .where(predicates.toArray(new Predicate[]{}))
                    .getRestriction();
        };
    }

    private Specification<History> getAllHistorySpecification(SearchHistoryRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getTransactionType() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("transactionType")),
                        "%" + request.getTransactionType().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }

            if (request.getAccountID() != null) {
                Join<User, History> userHistoryJoin = root.join("user", JoinType.INNER);

                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(userHistoryJoin.get("accountID")),
                        "%" + request.getAccountID().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }

            if (request.getStartTransactionDate() != null && request.getEndTransactionDate() != null) {
                Predicate predicate = criteriaBuilder.between(
                        root.get("transactionDate"),
                        request.getStartTransactionDate(),
                        request.getEndTransactionDate()
                );
                predicates.add(predicate);
            }

            return query
                    .where(predicates.toArray(new Predicate[]{}))
                    .getRestriction();
        };
    }
}
