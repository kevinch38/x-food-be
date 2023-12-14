package com.enigma.x_food.feature.pin;

import com.enigma.x_food.feature.pin.Pin;
import com.enigma.x_food.feature.pin.dto.request.*;
import com.enigma.x_food.feature.pin.dto.response.PinResponse;
import com.enigma.x_food.feature.pin.PinRepository;
import com.enigma.x_food.feature.pin.PinService;
import com.enigma.x_food.util.ValidationUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Timestamp;

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
                .build();
        Pin pin = Pin.builder()
                .pinID("1")
                .pin("2")
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .build();
        PinResponse pinResponse = PinResponse.builder()
                .pinID("1")
                .pin("2")
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .build();
        Mockito.doNothing().when(validationUtil).validate(newPinRequest);
        Mockito.when(pinRepository.saveAndFlush(pin)).thenReturn(pin);

        Pin actual = pinService.createNew(newPinRequest);

        assertEquals("1",actual.getPinID());
    }
}
