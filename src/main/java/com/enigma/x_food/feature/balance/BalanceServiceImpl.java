package com.enigma.x_food.feature.balance;

import com.enigma.x_food.feature.balance.dto.request.NewBalanceRequest;
import com.enigma.x_food.feature.balance.dto.request.UpdateBalanceRequest;
import com.enigma.x_food.feature.balance.dto.response.BalanceResponse;
import com.enigma.x_food.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class BalanceServiceImpl implements BalanceService {
    private final BalanceRepository balanceRepository;
    private final ValidationUtil validationUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Balance createNew(NewBalanceRequest request) {
        try {
            log.info("Start createNew");
            validationUtil.validate(request);

            Balance balance = Balance.builder()
                    .totalBalance(request.getTotalBalance())
                    .build();

            balanceRepository.save(balance);
            log.info("End createNew");
            return balance;
        } catch (DataIntegrityViolationException e) {
            log.error("Error createNew: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "balance already exist");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BalanceResponse update(UpdateBalanceRequest request) {
        validationUtil.validate(request);
        Balance balance = findByIdOrThrowException(request.getBalanceID());
        balance.setTotalBalance(request.getTotalBalance());

        return mapToResponse(balanceRepository.saveAndFlush(balance));
    }

    @Override
    @Transactional(readOnly = true)
    public BalanceResponse findById(String id) {
        validationUtil.validate(id);
        return mapToResponse(findByIdOrThrowException(id));
    }

    @Override
    public Balance getById(String balanceID) {
        return findByIdOrThrowException(balanceID);
    }

    private Balance findByIdOrThrowException(String id) {
        return balanceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Balance not found"));
    }

    private BalanceResponse mapToResponse(Balance balance) {
        return BalanceResponse.builder()
                .balanceID(balance.getBalanceID())
                .accountID(balance.getUser().getAccountID())
                .totalBalance(balance.getTotalBalance())
                .build();
    }
}
