package com.enigma.x_food.feature.otp;

import com.enigma.x_food.feature.otp.OTPService;
import com.enigma.x_food.feature.otp.dto.request.NewOTPRequest;
import com.enigma.x_food.feature.otp.dto.response.OTPResponse;
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
class OTPControllerTest {
    @MockBean
    private OTPService otpService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void createNewOTP() throws Exception {
        NewOTPRequest otp = NewOTPRequest.builder()
                .otp("2")
                .accountID("3")
                .build();
        OTPResponse promotionResponse = OTPResponse.builder()
                .otpID("1")
                .otp("2")
                .accountID("3")
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .build();
        Mockito.when(otpService.createNew(otp)).thenReturn(promotionResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/otp")
                        .content(objectMapper.writeValueAsString(otp))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(result -> {
                    CommonResponse<OTPResponse> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {}
                    );
                    Assertions.assertEquals(201, response.getStatusCode());
                    Assertions.assertEquals("3",response.getData().getAccountID());
                });
    }
}