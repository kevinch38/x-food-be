package com.enigma.x_food.feature.merchant;

import com.enigma.x_food.feature.merchant.dto.request.NewMerchantRequest;
import com.enigma.x_food.feature.merchant.dto.request.SearchMerchantRequest;
import com.enigma.x_food.feature.merchant.dto.request.UpdateMerchantRequest;
import com.enigma.x_food.feature.merchant.dto.response.MerchantResponse;
import com.enigma.x_food.shared.CommonResponse;
import com.enigma.x_food.shared.PagingResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class MerchantControllerTest {

    @MockBean
    private MerchantService merchantService;

    @Autowired
    private MerchantController merchantController;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void createNewMerchant() {
        NewMerchantRequest request = new NewMerchantRequest();

        MerchantResponse expectedResponse = new MerchantResponse();

        when(merchantService.createNew(request)).thenReturn(expectedResponse);

        ResponseEntity<?> responseEntity = merchantController.createNewMerchant(request);

        CommonResponse<MerchantResponse> responseBody = (CommonResponse<MerchantResponse>) responseEntity.getBody();

        assert responseBody != null;
        assert responseEntity.getStatusCodeValue() == HttpStatus.CREATED.value();
        assert responseBody.getData().equals(expectedResponse);
    }

    @Test
    public void testGetAllMerchant() throws Exception {
        Page<MerchantResponse> mockMerchants = new PageImpl<>(Collections.emptyList());
        when(merchantService.getAll(any())).thenReturn(mockMerchants);

        mockMvc.perform(get("/api/merchants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("successfully get all merchant"))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.data").isArray());

        verify(merchantService, times(1)).getAll(any());
    }

    @Test
    void updateMerchant() {
        UpdateMerchantRequest request = new UpdateMerchantRequest();

        MerchantResponse expectedResponse = new MerchantResponse();

        when(merchantService.update(request)).thenReturn(expectedResponse);

        ResponseEntity<?> responseEntity = merchantController.updateMerchant(request);

        CommonResponse<MerchantResponse> responseBody = (CommonResponse<MerchantResponse>) responseEntity.getBody();

        assert responseBody != null;
        assert responseEntity.getStatusCodeValue() == HttpStatus.OK.value();
        assert responseBody.getData().equals(expectedResponse);
    }

    @Test
    void deleteMerchantById() {
        String merchantId = "123";

        ResponseEntity<?> responseEntity = merchantController.deleteMerchantById(merchantId);

        CommonResponse<?> responseBody = (CommonResponse<?>) responseEntity.getBody();

        assert responseBody != null;
        assert responseEntity.getStatusCodeValue() == HttpStatus.OK.value();
    }
}
