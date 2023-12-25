package com.enigma.x_food.feature.payment;

import com.enigma.x_food.constant.EPaymentStatus;
import com.enigma.x_food.constant.EPaymentType;
import com.enigma.x_food.constant.ETransactionType;
import com.enigma.x_food.feature.friend.Friend;
import com.enigma.x_food.feature.friend.FriendService;
import com.enigma.x_food.feature.friend.dto.request.SearchFriendRequest;
import com.enigma.x_food.feature.history.History;
import com.enigma.x_food.feature.history.HistoryService;
import com.enigma.x_food.feature.history.dto.request.HistoryRequest;
import com.enigma.x_food.feature.order.Order;
import com.enigma.x_food.feature.order.OrderRepository;
import com.enigma.x_food.feature.payment.dto.request.SearchPaymentRequest;
import com.enigma.x_food.feature.payment.dto.request.PaymentRequest;
import com.enigma.x_food.feature.payment.dto.request.SplitBillRequest;
import com.enigma.x_food.feature.payment.dto.response.PaymentResponse;
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
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentStatusService paymentStatusService;
    private final FriendService friendService;
    private final UserService userService;
    private final HistoryService historyService;
    private final OrderRepository orderRepository;
    private final ValidationUtil validationUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Payment createNew(PaymentRequest request) {
        log.info("Start createNew");
        validationUtil.validate(request);

        PaymentStatus paymentStatus = paymentStatusService.getByStatus(EPaymentStatus.PENDING);

        Payment payment = Payment.builder()
                .paymentAmount(request.getPaymentAmount())
                .user(request.getUser())
                .paymentAmount(request.getPaymentAmount())
                .paymentType("ORDER")
                .expiredAt(request.getExpiredAt())
                .paymentStatus(paymentStatus)
                .history(request.getHistory())
                .order(request.getOrder())
                .build();

        paymentRepository.saveAndFlush(payment);
        log.info("End createNew");
        return payment;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<PaymentResponse> createSplitBill(List<SplitBillRequest> splitBillRequests) {
        log.info("Start createNew");
        validationUtil.validate(splitBillRequests);

        List<Payment> payments = splitBillRequests.stream().map(
                request -> {
                    PaymentStatus paymentStatus = paymentStatusService.getByStatus(EPaymentStatus.PENDING);

                    SearchFriendRequest friendRequest = SearchFriendRequest.builder()
                            .friendID(request.getFriendID())
                            .accountID(request.getAccountID())
                            .build();
                    List<Friend> friend = friendService.findByFriendId(friendRequest);

                    User user = userService.getUserById(request.getAccountID());
                    Order order = orderRepository.findById(request.getOrderID())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

                    Instant currentTime = Instant.now();
                    Instant expiredTime = currentTime.plus(Duration.ofHours(1));
                    Timestamp expiredAt = Timestamp.from(expiredTime);
                    return Payment.builder()
                            .paymentAmount(request.getPaymentAmount())
                            .user(user)
                            .paymentAmount(request.getPaymentAmount())
                            .paymentType(EPaymentType.SPLIT_BILL.name())
                            .expiredAt(expiredAt)
                            .paymentStatus(paymentStatus)
                            .friend(friend.get(0))
                            .order(order)
                            .build();

                }).collect(Collectors.toList());

        paymentRepository.saveAllAndFlush(payments);
        for (Payment payment : payments) {
            User user = userService.getUserById(payment.getUser().getAccountID());
            HistoryRequest historyRequest = HistoryRequest.builder()
                    .transactionType(ETransactionType.PAYMENT.name())
                    .historyValue(payment.getPaymentAmount())
                    .transactionDate(LocalDate.now())
                    .credit(true)
                    .debit(false)
                    .orderID(null)
                    .paymentID(payment.getPaymentID())
                    .topUpID(null)
                    .accountID(user.getAccountID())
                    .build();
            History history = historyService.createNew(historyRequest);

            payment.setHistory(history);
        }
        log.info("End createNew");
        return payments.stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> findByAccountId(SearchPaymentRequest request) {
        Specification<Payment> specification = getPaymentSpecification(request);
        return paymentRepository.findAll(specification).stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private PaymentResponse mapToResponse(Payment payment) {
        String friendID = null;
        if (payment.getFriend() != null)
            friendID = payment.getFriend().getFriendID();

        return PaymentResponse.builder()
                .paymentID(payment.getPaymentID())
                .paymentAmount(payment.getPaymentAmount())
                .accountID(payment.getUser().getAccountID())
                .paymentAmount(payment.getPaymentAmount())
                .paymentType(payment.getPaymentType())
                .expiredAt(payment.getExpiredAt())
                .paymentStatus(payment.getPaymentStatus().getStatus().name())
                .historyID(payment.getHistory().getHistoryID())
                .friendID(friendID)
                .orderID(payment.getOrder().getOrderID())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }

    private Specification<Payment> getPaymentSpecification(SearchPaymentRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getAccountID() != null) {
                Join<Payment, History> paymentHistoryJoin = root.join("history", JoinType.INNER);

                Predicate predicate = criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("user").get("accountID")),
                        request.getAccountID().toLowerCase()
                );
                predicates.add(predicate);

                predicate = criteriaBuilder.equal(
                        criteriaBuilder.lower(paymentHistoryJoin.get("transactionType")),
                        "payment"
                );
                predicates.add(predicate);
            }
            return query
                    .where(predicates.toArray(new Predicate[]{}))
                    .getRestriction();
        };
    }
}
