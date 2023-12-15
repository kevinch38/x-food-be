package com.enigma.x_food.feature.merchant;

import com.enigma.x_food.feature.merchant.dto.request.NewMerchantRequest;
import com.enigma.x_food.feature.merchant.dto.request.SearchMerchantRequest;
import com.enigma.x_food.feature.merchant.dto.request.UpdateMerchantRequest;
import com.enigma.x_food.feature.merchant.dto.response.MerchantResponse;
import com.enigma.x_food.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class MerchantServiceImplTest {

    @MockBean
    private MerchantRepository merchantRepository;

    @MockBean
    private ValidationUtil validationUtil;

    @Autowired
    private MerchantServiceImpl merchantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateNew() {
        NewMerchantRequest request = new NewMerchantRequest(/* provide necessary parameters */);

        doNothing().when(validationUtil).validate(request);

        Merchant savedMerchant = Merchant.builder().merchantID("1").build();
        when(merchantRepository.saveAndFlush(any(Merchant.class))).thenReturn(savedMerchant);

        MerchantResponse response = merchantService.createNew(request);

        assertNotNull(response);
    }

    @Test
    void testUpdate() {
        UpdateMerchantRequest request = UpdateMerchantRequest.builder().merchantID("1").build();

        doNothing().when(validationUtil).validate(request);

        Merchant existingMerchant = Merchant.builder().merchantID("1").build();
        when(merchantRepository.findById(anyString())).thenReturn(java.util.Optional.of(existingMerchant));
        Merchant updatedMerchant = Merchant.builder().merchantID("1").picEmail("email").build();
        when(merchantRepository.saveAndFlush(any(Merchant.class))).thenReturn(updatedMerchant);

        MerchantResponse response = merchantService.update(request);

        assertNotNull(response);
        assertEquals("email", response.getPicEmail());
    }

    @Test
    void testFindById_WhenMerchantExists() {
        String merchantId = "1";
        Merchant existingMerchant = Merchant.builder().merchantID(merchantId).build();
        when(merchantRepository.findById(merchantId)).thenReturn(java.util.Optional.of(existingMerchant));

        MerchantResponse response = merchantService.findById(merchantId);

        assertNotNull(response);
        assertEquals("1", response.getMerchantID());

    }

    @Test
    void testFindById_WhenMerchantDoesNotExist() {
        String merchantId = "nonExistingMerchantId";
        when(merchantRepository.findById(merchantId)).thenReturn(java.util.Optional.empty());

        assertThrows(ResponseStatusException.class, () -> merchantService.findById(merchantId));
    }

    @Test
    void testDeleteById() {
        String merchantId = "someMerchantId";
        Merchant existingMerchant = Merchant.builder().merchantID("1").build();
        when(merchantRepository.findById(merchantId)).thenReturn(java.util.Optional.of(existingMerchant));

        merchantService.deleteById(merchantId);

        verify(merchantRepository, times(1)).delete(existingMerchant);
    }

    @Test
    void testGetAll() {
        SearchMerchantRequest request = SearchMerchantRequest.builder()
                .page(1)
                .size(5)
                .sortBy("merchantID")
                .direction("asc")
                .build();
        List<Merchant> merchantList = Arrays.asList(
                Merchant.builder().merchantID("1").build()
        );

        Pageable pageable = Pageable.unpaged();

        Page<Merchant> mockPage = new PageImpl<>(merchantList, pageable, merchantList.size());
        when(merchantRepository.findAll(any(Pageable.class))).thenReturn(mockPage);

        Page<MerchantResponse> responsePage = merchantService.getAll(request);

        assertNotNull(responsePage);
    }
}