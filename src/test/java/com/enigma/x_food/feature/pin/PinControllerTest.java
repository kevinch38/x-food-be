package com.enigma.x_food.feature.pin;

import com.enigma.x_food.feature.pin.dto.request.CheckPinRequest;
import com.enigma.x_food.feature.pin.dto.request.UpdatePinRequest;
import com.enigma.x_food.feature.pin.dto.response.PinResponse;
import com.enigma.x_food.shared.CommonResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.sql.Timestamp;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class PinControllerTest {
    @MockBean
    private PinService pinService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Test
    void createNewPin() throws Exception {
        UpdatePinRequest pin = UpdatePinRequest.builder()
                .pin("2")
                .build();
        PinResponse pinResponse = PinResponse.builder()
                .pinID("1")
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .build();
        Mockito.when(pinService.update(pin)).thenReturn(pinResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/pins")
                        .content(objectMapper.writeValueAsString(pin))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(result -> {
                    CommonResponse<PinResponse> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {}
                    );
                    Assertions.assertEquals(201, response.getStatusCode());
                    Assertions.assertEquals("1",response.getData().getPinID());
                });
    }

    @Test
    void checkPin() throws Exception {
        CheckPinRequest pin = CheckPinRequest.builder()
                .pinID("1")
                .pin("2")
                .build();
//        Pin pinResponse = Pin.builder()
//                .pinID("1")
//                .pin("2")
//                .createdAt(new Timestamp(System.currentTimeMillis()))
//                .updatedAt(new Timestamp(System.currentTimeMillis()))
//                .build();
        Mockito.when(pinService.checkPin(pin)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/pins")
                        .content(objectMapper.writeValueAsString(pin))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(result -> {
                    CommonResponse<Boolean> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {}
                    );
                    Assertions.assertEquals(200, response.getStatusCode());
                    Assertions.assertEquals(true, response.getData());
                });
    }

    @Test
    void getPinById() throws Exception {
        String id = "1";
        PinResponse pinResponse = PinResponse.builder()
                .pinID("1")
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .build();

        Mockito.when(pinService.getById(Mockito.any())).thenReturn(pinResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/pins/"+id)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(result -> {
                    CommonResponse<PinResponse> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {}
                    );
                    Assertions.assertEquals(200, response.getStatusCode());
                    Assertions.assertEquals("1",response.getData().getPinID());
                });
    }



}