package com.enigma.x_food.service.impl;

import com.enigma.x_food.feature.pin.dto.request.*;
import com.enigma.x_food.feature.pin.dto.response.PinResponse;
import com.enigma.x_food.feature.pin.entity.*;
import com.enigma.x_food.feature.pin.repository.PinRepository;
import com.enigma.x_food.feature.pin.service.PinService;
import com.enigma.x_food.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PinServiceImplTest {
    @Autowired
    private PinService pinService;
    @MockBean
    private PinRepository pinRepository;
    @MockBean
    private ValidationUtil validationUtil;

    @Test
    void createPin() {
        NewPinRequest newPinRequest = NewPinRequest.builder()
                .pin("2")
                .accountID("3")
                .build();
        Pin pin = Pin.builder()
                .pinID("1")
                .pin("2")
                .accountID("3")
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .build();
        PinResponse pinResponse = PinResponse.builder()
                .pinID("1")
                .pin("2")
                .accountID("3")
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .build();
        Mockito.doNothing().when(validationUtil).validate(newPinRequest);
        Mockito.when(pinRepository.saveAndFlush(pin)).thenReturn(pin);

        PinResponse actual = pinService.createNew(newPinRequest);

        assertEquals("3",actual.getAccountID());
    }
}
