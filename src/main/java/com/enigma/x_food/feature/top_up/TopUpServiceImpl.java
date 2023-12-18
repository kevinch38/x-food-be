package com.enigma.x_food.feature.top_up;

import com.enigma.x_food.feature.history.History;
import com.enigma.x_food.feature.history.HistoryService;
import com.enigma.x_food.feature.history.dto.request.HistoryRequest;
import com.enigma.x_food.feature.top_up.dto.request.SearchTopUpRequest;
import com.enigma.x_food.feature.top_up.dto.request.TopUpRequest;
import com.enigma.x_food.feature.top_up.dto.response.TopUpResponse;
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
public class TopUpServiceImpl implements TopUpService {
    private final TopUpRepository topUpRepository;
    private final HistoryService historyService;
    private final ValidationUtil validationUtil;
    private final EntityManager entityManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TopUpResponse createNew(TopUpRequest request) {
        try {
            log.info("Start createNew");
            validationUtil.validate(request);

            HistoryRequest historyRequest = HistoryRequest.builder()
                    .transactionType("TOPUP")
                    .historyValue(request.getTopUpAmount())
                    .transactionDate(LocalDate.now())
                    .credit(false)
                    .debit(true)
                    .orderID(null)
                    .paymentID(null)
                    .accountID(request.getAccountID())
                    .build();

            History history = historyService.createNew(historyRequest);

            TopUp topUp = TopUp.builder()
                    .topUpAmount(request.getTopUpAmount())
                    .methodID(request.getMethodID())
                    .topUpFee(request.getTopUpFee())
                    .topUpStatusID(request.getTopUpStatusID())
                    .balanceID(request.getBalanceID())
                    .history(entityManager.merge(history))
                    .build();

            history.setTopUp(topUp);

            topUpRepository.saveAndFlush(topUp);
            log.info("End createNew");
            return mapToResponse(topUp);
        } catch (DataIntegrityViolationException e) {
            log.error("Error createNew: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "top up already exist");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<TopUpResponse> findByAccountId(SearchTopUpRequest request) {
        Specification<TopUp> specification = getTopUpSpecification(request);
        return topUpRepository.findAll(specification).stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private TopUpResponse mapToResponse(TopUp topUp) {
        return TopUpResponse.builder()
                .topUpID(topUp.getTopUpID())
                .topUpAmount(topUp.getTopUpAmount())
                .methodID(topUp.getMethodID())
                .topUpFee(topUp.getTopUpFee())
                .topUpStatusID(topUp.getTopUpStatusID())
                .balanceID(topUp.getBalanceID())
                .historyID(topUp.getHistory().getHistoryID())
                .createdAt(topUp.getCreatedAt())
                .updatedAt(topUp.getUpdatedAt())
                .build();
    }

    private Specification<TopUp> getTopUpSpecification(SearchTopUpRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getAccountID() != null) {
                Join<TopUp, History> topUpHistoryJoin = root.join("history", JoinType.INNER);

                Predicate predicate = criteriaBuilder.equal(
                        criteriaBuilder.lower(topUpHistoryJoin.get("user").get("accountID")),
                        request.getAccountID().toLowerCase()
                );
                predicates.add(predicate);

                predicate = criteriaBuilder.equal(
                        criteriaBuilder.lower(topUpHistoryJoin.get("transactionType")),
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
