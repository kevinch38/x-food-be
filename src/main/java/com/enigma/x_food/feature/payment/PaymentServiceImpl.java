package com.enigma.x_food.feature.payment;

import com.enigma.x_food.constant.EPaymentType;
import com.enigma.x_food.feature.history.History;
import com.enigma.x_food.feature.history.HistoryService;
import com.enigma.x_food.feature.history.dto.request.HistoryRequest;
import com.enigma.x_food.feature.order.Order;
import com.enigma.x_food.feature.order.OrderService;
import com.enigma.x_food.feature.payment.dto.request.SearchPaymentRequest;
import com.enigma.x_food.feature.payment.dto.request.PaymentRequest;
import com.enigma.x_food.feature.payment.dto.response.PaymentResponse;
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
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final HistoryService historyService;
    private final UserService userService;
    private final OrderService orderService;
    private final ValidationUtil validationUtil;
    private final EntityManager entityManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentResponse createNew(PaymentRequest request) {
        try {
            log.info("Start createNew");
            validationUtil.validate(request);

            HistoryRequest historyRequest = HistoryRequest.builder()
                    .transactionType("PAYMENT")
                    .historyValue(request.getPaymentAmount())
                    .transactionDate(LocalDate.now())
                    .credit(false)
                    .debit(true)
                    .orderID(null)
                    .topUpID(null)
                    .accountID(request.getAccountID())
                    .build();

            History history = historyService.createNew(historyRequest);
            User user = userService.getUserById(request.getAccountID());
            Order order = orderService.findById(request.getOrderID());

            Payment payment = Payment.builder()
                    .paymentAmount(request.getPaymentAmount())
                    .user(entityManager.merge(user))
                    .paymentAmount(request.getPaymentAmount())
                    .paymentType(EPaymentType.valueOf(request.getPaymentType()).toString())
                    .expiredAt(request.getExpiredAt())
                    .paymentStatusID(request.getPaymentStatusID())
                    .history(entityManager.merge(history))
                    .friendID(request.getFriendID())
                    .order(entityManager.merge(order))
                    .build();

            history.setPayment(payment);

            paymentRepository.saveAndFlush(payment);
            log.info("End createNew");
            return mapToResponse(payment);
        } catch (DataIntegrityViolationException e) {
            log.error("Error createNew: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "top up already exist");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> findByAccountId(SearchPaymentRequest request) {
        Specification<Payment> specification = getPaymentSpecification(request);
        return paymentRepository.findAll(specification).stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private PaymentResponse mapToResponse(Payment payment) {
        return PaymentResponse.builder()
                .paymentID(payment.getPaymentID())
                .paymentAmount(payment.getPaymentAmount())
                .accountID(payment.getUser().getAccountID())
                .paymentAmount(payment.getPaymentAmount())
                .paymentType(payment.getPaymentType())
                .expiredAt(payment.getExpiredAt())
                .paymentStatusID(payment.getPaymentStatusID())
                .historyID(payment.getHistory().getHistoryID())
                .friendID(payment.getFriendID())
                .orderID(payment.getOrder().getOrderID())
                .build();
    }

    private Specification<Payment> getPaymentSpecification(SearchPaymentRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getAccountID() != null) {
                Join<Payment, History> paymentHistoryJoin = root.join("history", JoinType.INNER);

                Predicate predicate = criteriaBuilder.equal(
                        criteriaBuilder.lower(paymentHistoryJoin.get("user").get("accountID")),
                        request.getAccountID().toLowerCase()
                );
                predicates.add(predicate);

                predicate = criteriaBuilder.equal(
                        criteriaBuilder.lower(paymentHistoryJoin.get("transactionType")),
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
