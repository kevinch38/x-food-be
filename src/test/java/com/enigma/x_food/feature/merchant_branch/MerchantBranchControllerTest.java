package com.enigma.x_food.feature.merchant_branch;

import com.enigma.x_food.feature.merchant_branch.dto.request.*;
import com.enigma.x_food.feature.merchant_branch.dto.response.*;
import com.enigma.x_food.shared.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class MerchantBranchControllerTest {
    @MockBean
    private MerchantBranchService merchantBranchService;
    @Autowired
    private MerchantBranchController merchantBranchController;

    @Test
    void testCreateNewMerchantBranch() {
        NewMerchantBranchRequest request = NewMerchantBranchRequest.builder().merchantID("1").build();
        MerchantBranchResponse mockResponse = MerchantBranchResponse.builder().branchID("1").build();
        when(merchantBranchService.createNew(any(NewMerchantBranchRequest.class))).thenReturn(mockResponse);

        ResponseEntity<?> responseEntity = merchantBranchController.createNewMerchantBranch(request);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        CommonResponse<?> responseBody = (CommonResponse<?>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("successfully create new merchant branch", responseBody.getMessage());
        assertEquals(HttpStatus.CREATED.value(), responseBody.getStatusCode());

        MerchantBranchResponse responseData = (MerchantBranchResponse) responseBody.getData();
        assertNotNull(responseData);
        assertEquals("1", responseData.getBranchID());
    }

    @Test
    void testGetAllMerchantBranch() {
        SearchMerchantBranchRequest request = SearchMerchantBranchRequest.builder()
                .page(1)
                .size(10)
                .direction("asc")
                .sortBy("branchID")
                .build();
        List<MerchantBranchResponse> merchantBranchResponseList = Arrays.asList(
               MerchantBranchResponse.builder().branchID("1").build()
        );

        when(merchantBranchService.findByMerchantId(any(SearchMerchantBranchRequest.class))).thenReturn(merchantBranchResponseList);

        ResponseEntity<?> responseEntity = merchantBranchController.findAll(
                request.getPage(),
                request.getSize(),
                request.getDirection(),
                request.getSortBy(),
                request.getBranchID(),
                request.getMerchantID(),
                request.getBranchName(),
                request.getAddress(),
                request.getTimezone(),
                request.getBranchWorkingHoursID(),
                request.getCityID()
        );

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        CommonResponse<?> responseBody = (CommonResponse<?>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("successfully get all merchant branch", responseBody.getMessage());
        assertEquals(HttpStatus.OK.value(), responseBody.getStatusCode());

        List<MerchantBranchResponse> responseData = (List<MerchantBranchResponse>) responseBody.getData();
        assertNotNull(responseData);
        assertEquals(1, responseData.size());
    }

    @Test
    void testUpdateMerchantBranch() {
        UpdateMerchantBranchRequest request = new UpdateMerchantBranchRequest(/* provide necessary parameters */);
        MerchantBranchResponse mockResponse = new MerchantBranchResponse(/* provide necessary parameters */);
        when(merchantBranchService.update(any(UpdateMerchantBranchRequest.class))).thenReturn(mockResponse);

        ResponseEntity<?> responseEntity = merchantBranchController.updateMerchantBranch(request);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        CommonResponse<?> responseBody = (CommonResponse<?>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("successfully update merchant branch", responseBody.getMessage());
        assertEquals(HttpStatus.OK.value(), responseBody.getStatusCode());

        MerchantBranchResponse responseData = (MerchantBranchResponse) responseBody.getData();
        assertNotNull(responseData);
    }

    @Test
    void testDeleteMerchantBranchById() {
        String branchId = "someBranchId";
        doNothing().when(merchantBranchService).deleteById(branchId);

        ResponseEntity<?> responseEntity = merchantBranchController.deleteMerchantBranchById(branchId);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        CommonResponse<?> responseBody = (CommonResponse<?>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("successfully delete merchant branch", responseBody.getMessage());
        assertEquals(HttpStatus.OK.value(), responseBody.getStatusCode());
        assertEquals("OK", responseBody.getData());
    }
}
