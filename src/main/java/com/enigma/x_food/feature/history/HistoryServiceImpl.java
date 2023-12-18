package com.enigma.x_food.feature.history;

import com.enigma.x_food.constant.ETransactionType;
import com.enigma.x_food.feature.history.dto.request.HistoryRequest;
import com.enigma.x_food.feature.history.dto.request.SearchHistoryRequest;
import com.enigma.x_food.feature.history.dto.response.HistoryResponse;
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
public class HistoryServiceImpl implements HistoryService {
    private final HistoryRepository historyRepository;
    private final UserService userService;
    private final ValidationUtil validationUtil;
    private final EntityManager entityManager;

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
                    .user(entityManager.merge(user))
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
    public List<HistoryResponse> findByAccountId(SearchHistoryRequest request) {
        Specification<History> specification = getHistorySpecification(request);
        List<History> histories = historyRepository.findAll(specification);
        return histories.stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private HistoryResponse mapToResponse(History history) {
        return HistoryResponse.builder()
                .historyID(history.getHistoryID())
                .transactionType(history.getTransactionType().toString())
                .historyValue(history.getHistoryValue())
                .transactionDate(LocalDate.now())
                .credit(history.getCredit())
                .debit(history.getDebit())
                .orderID(history.getOrderID())
                .paymentID(history.getPaymentID())
                .topUpID(history.getTopUp().getTopUpID())
                .accountID(history.getUser().getAccountID())
                .build();
    }

    private Specification<History> getHistorySpecification(SearchHistoryRequest request) {
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
}
