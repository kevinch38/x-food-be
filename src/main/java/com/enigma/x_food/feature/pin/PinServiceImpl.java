package com.enigma.x_food.feature.pin;

import com.enigma.x_food.feature.pin.dto.request.CheckPinRequest;
import com.enigma.x_food.feature.pin.dto.request.NewPinRequest;
import com.enigma.x_food.feature.pin.dto.request.UpdatePinRequest;
import com.enigma.x_food.feature.pin.dto.response.PinResponse;
import com.enigma.x_food.security.BCryptUtil;
import com.enigma.x_food.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class PinServiceImpl implements PinService {
    private final PinRepository pinRepository;
    private final BCryptUtil bCryptUtil;
    private final ValidationUtil validationUtil;
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Pin createNew(NewPinRequest request) {
        try {
            validationUtil.validate(request);
            Pin pin = Pin.builder()
                    .pin(request.getPin())
                    .build();
            pinRepository.saveAndFlush(pin);
            return pin;
        } catch (Exception e) {
            log.error("Error createNew: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PinResponse update(UpdatePinRequest request) {
        try {
            validationUtil.validate(request);
            Pin pin = findByIdOrThrowNotFound(request.getPinID());
            pin.setPin(bCryptUtil.hash(request.getPin()));
            pinRepository.saveAndFlush(pin);
            return mapToResponse(pin);
        } catch (Exception e) {
            log.error("Error createNew: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    @Override
    public PinResponse getById(String id) {
        return mapToResponse(findByIdOrThrowNotFound(id));
    }

    @Override
    public boolean checkPin(CheckPinRequest request) {
        validationUtil.validate(request);
        Pin pin = findByIdOrThrowNotFound(request.getPinID());
        return bCryptUtil.check(request.getPin(), pin.getPin());
    }

    private Pin findByIdOrThrowNotFound(String id) {
        return pinRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "pin not found"));
    }

    private PinResponse mapToResponse(Pin pin) {
        return PinResponse.builder()
                .pinID(pin.getPinID())
                .pin(pin.getPin())
                .createdAt(pin.getCreatedAt())
                .updatedAt(pin.getUpdatedAt())
                .build();
    }
}
