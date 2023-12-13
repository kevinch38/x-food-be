package com.enigma.x_food.feature.merchant_branch;

import com.enigma.x_food.feature.merchant_branch.dto.request.NewMerchantBranchRequest;
import com.enigma.x_food.feature.merchant_branch.dto.request.UpdateMerchantBranchRequest;
import com.enigma.x_food.feature.merchant_branch.dto.response.MerchantBranchResponse;
import com.enigma.x_food.feature.merchant_branch.dto.request.SearchMerchantBranchRequest;
import com.enigma.x_food.shared.CommonResponse;
import com.enigma.x_food.shared.PagingResponse;
import com.enigma.x_food.util.PagingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/merchants/branch")
@RequiredArgsConstructor
public class MerchantBranchController {
    private final MerchantBranchService merchantBranchService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNewMerchantBranch(@RequestBody NewMerchantBranchRequest request) {
        MerchantBranchResponse merchantBranchResponse = merchantBranchService.createNew(request);
        CommonResponse<MerchantBranchResponse> response = CommonResponse.<MerchantBranchResponse>builder()
                .message("successfully create new merchant branch")
                .statusCode(HttpStatus.CREATED.value())
                .data(merchantBranchResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<?> getAllMerchantBranch(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(required = false, defaultValue = "accountID") String sortBy,
            @RequestParam(required = false) String branchID,
            @RequestParam(required = false) String merchantId,
            @RequestParam(required = false) String branchName,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String timeZone,
            @RequestParam(required = false) String branchWorkingHoursID,
            @RequestParam(required = false) String cityID
    ) {
        page = PagingUtil.validatePage(page);
        size = PagingUtil.validateSize(size);
        direction = PagingUtil.validateDirection(direction);

        SearchMerchantBranchRequest request = SearchMerchantBranchRequest.builder()
                .page(page)
                .size(size)
                .direction(direction)
                .sortBy(sortBy)
                .branchID(branchID)
                .merchantID(merchantId)
                .branchName(branchName)
                .address(address)
                .timezone(timeZone)
                .branchWorkingHoursID(branchWorkingHoursID)
                .cityID(cityID)
                .build();

        Page<MerchantBranchResponse> merchantBranches = merchantBranchService.getAll(request);

        PagingResponse pagingResponse = PagingResponse.builder()
                .page(page)
                .size(size)
                .count(merchantBranches.getTotalElements())
                .totalPages(merchantBranches.getTotalPages())
                .build();

        CommonResponse<List<MerchantBranchResponse>> response = CommonResponse.<List<MerchantBranchResponse>>builder()
                .message("successfully get all merchant branch")
                .statusCode(HttpStatus.OK.value())
                .data(merchantBranches.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping
    public ResponseEntity<?> updateMerchantBranch(@RequestBody UpdateMerchantBranchRequest request) {
        MerchantBranchResponse merchantBranchResponse = merchantBranchService.update(request);
        CommonResponse<MerchantBranchResponse> response = CommonResponse.<MerchantBranchResponse>builder()
                .message("successfully update merchant branch")
                .statusCode(HttpStatus.OK.value())
                .data(merchantBranchResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMerchantBranchById(@PathVariable String id) {
        merchantBranchService.deleteById(id);
        CommonResponse<?> response = CommonResponse.builder()
                .message("successfully update merchant branch")
                .statusCode(HttpStatus.OK.value())
                .data("OK")
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
