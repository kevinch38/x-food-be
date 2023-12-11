package com.enigma.x_food.feature.pin.service.impl;

import com.enigma.x_food.feature.pin.dto.request.NewPinRequest;
import com.enigma.x_food.feature.pin.service.PinService;
import com.enigma.x_food.feature.pin.dto.request.SearchPinRequest;
import com.enigma.x_food.feature.pin.dto.response.PinResponse;
import com.enigma.x_food.feature.pin.entity.Pin;
import com.enigma.x_food.feature.pin.repository.PinRepository;
import com.enigma.x_food.security.BCryptUtil;
import com.enigma.x_food.util.SortingUtil;
import com.enigma.x_food.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
@Slf4j
public class PinServiceImpl implements PinService {
    private final PinRepository pinRepository;
    private final BCryptUtil bCryptUtil;
    private final ValidationUtil validationUtil;
    @Transactional(rollbackFor = Exception.class)
    @Override
    public PinResponse createNew(NewPinRequest request) {
        try {
            log.info("Start createNew");
            validationUtil.validate(request);
            Pin pin = Pin.builder()
                    .pin(bCryptUtil.hashPassword(request.getPin()))
                    .createdAt(new Timestamp(System.currentTimeMillis()))
                    .updatedAt(new Timestamp(System.currentTimeMillis()))
                    .accountID(request.getAccountID())
                    .build();
            pinRepository.saveAndFlush(pin);
            log.info("End createNew");
            return mapToResponse(pin);
        } catch (DataIntegrityViolationException e) {
            log.error("Error createNew: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "pin already exist");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PinResponse> getAll(SearchPinRequest request) {
        log.info("Start getAll");
        String fieldName = SortingUtil.sortByValidation(Pin.class, request.getSortBy(), "pinID");
        request.setSortBy(fieldName);

        Sort.Direction direction = Sort.Direction.fromString(request.getDirection());
        Pageable pageable = PageRequest.of(
                request.getPage() - 1,
                request.getSize(),
                direction,
                request.getSortBy()
        );

        Page<Pin> pins = pinRepository.findAll(pageable);
        log.info("End getAll");
        return pins.map(this::mapToResponse);
    }

    private PinResponse mapToResponse(Pin pin) {
        return PinResponse.builder()
                .pinID(pin.getPinID())
                .pin(pin.getPin())
                .accountID(pin.getAccountID())
                .createdAt(pin.getCreatedAt())
                .updatedAt(pin.getUpdatedAt())
                .build();
    }
}
