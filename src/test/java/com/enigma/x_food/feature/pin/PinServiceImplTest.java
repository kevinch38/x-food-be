package com.enigma.x_food.feature.pin;

import com.enigma.x_food.feature.pin.dto.request.*;
import com.enigma.x_food.feature.pin.dto.response.PinResponse;
import com.enigma.x_food.feature.pin.dto.request.NewPinRequest;
import com.enigma.x_food.feature.pin.dto.request.UpdatePinRequest;
import com.enigma.x_food.security.BCryptUtil;
import com.enigma.x_food.util.ValidationUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PinServiceImplTest {
    @Autowired
    private PinService pinService;
    @MockBean
    private PinRepository pinRepository;
    @Mock
    private BCryptUtil bCryptUtil;
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
                .build();
        Mockito.doNothing().when(validationUtil).validate(newPinRequest);
        Mockito.when(pinRepository.saveAndFlush(Mockito.isA(Pin.class))).thenReturn(pin);

        Pin actual = pinService.createNew(newPinRequest);

        assertEquals("2",actual.getPin());
    }

    @Test
    void update(){
        String id = "1";
        Pin pin = Pin.builder()
                .pinID("1")
                .pin("2")
                .build();
        Mockito.doNothing().when(validationUtil).validate(Mockito.isA(NewPinRequest.class));
        Mockito.when(pinRepository.findById(Mockito.anyString())).thenReturn(Optional.of(pin));
        Pin pin2 = Pin.builder()
                .pinID("1")
                .pin("3")
                .build();
        Mockito.when(pinRepository.saveAndFlush(Mockito.isA(Pin.class))).thenReturn(pin2);

        UpdatePinRequest request = UpdatePinRequest.builder()
                .pinID("1")
                .pin("3")
                .build();
        PinResponse pinById = pinService.update(request);

        assertEquals("1",pinById.getPinID());
    }
    @Test
    void getPinById(){
        String id = "1";
        Pin pin = Pin.builder()
                .pinID("1")
                .pin("2")
                .build();
        Mockito.when(pinRepository.findById(Mockito.anyString())).thenReturn(Optional.of(pin));

        PinResponse pinById = pinService.getById(id);

        assertEquals("1",pinById.getPinID());
    }
    @Test
    void checkPin() {
        Pin pin = Pin.builder()
                .pinID("1")
                .pin("2")
                .build();
        Mockito.doNothing().when(validationUtil).validate(Mockito.isA(CheckPinRequest.class));
        Mockito.when(pinRepository.findById(Mockito.anyString())).thenReturn(Optional.of(pin));

        CheckPinRequest request = CheckPinRequest.builder()
                .pinID("1")
                .pin("2")
                .build();
        pinService.checkPin(request);

        Mockito.verify(pinRepository, Mockito.times(1)).findById(Mockito.anyString());
    }
}
