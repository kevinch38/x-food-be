package com.enigma.x_food.feature.merchant_branch;

import com.enigma.x_food.feature.merchant_branch.dto.request.NewMerchantBranchRequest;
import com.enigma.x_food.feature.merchant_branch.dto.request.UpdateMerchantBranchRequest;
import com.enigma.x_food.feature.merchant_branch.dto.response.MerchantBranchResponse;
import com.enigma.x_food.feature.merchant_branch.dto.request.SearchMerchantBranchRequest;
import com.enigma.x_food.shared.CommonResponse;
import com.enigma.x_food.util.PagingUtil;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<?> createNew(@RequestBody NewMerchantBranchRequest request) {
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

    @GetMapping("/{merchantID}")
    public ResponseEntity<?> findAll(@PathVariable String merchantID,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(required = false, defaultValue = "branchName") String sortBy,
            @RequestParam(required = false) String branchID,
            @RequestParam(required = false) String branchName,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String timeZone,
            @RequestParam(required = false) String branchWorkingHoursID,
            @RequestParam(required = false) String cityID
    ) {
        direction = PagingUtil.validateDirection(direction);

        SearchMerchantBranchRequest request = SearchMerchantBranchRequest.builder()
                .direction(direction)
                .sortBy(sortBy)
                .branchID(branchID)
                .merchantID(merchantID)
                .branchName(branchName)
                .address(address)
                .timezone(timeZone)
                .branchWorkingHoursID(branchWorkingHoursID)
                .cityID(cityID)
                .build();

        List<MerchantBranchResponse> merchantBranches = merchantBranchService.findAllByMerchantId(request);

        CommonResponse<List<MerchantBranchResponse>> response = CommonResponse.<List<MerchantBranchResponse>>builder()
                .message("successfully get all merchant branch")
                .statusCode(HttpStatus.OK.value())
                .data(merchantBranches)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{merchantID}/active")
    public ResponseEntity<?> getAll(@PathVariable String merchantID,
                                     @RequestParam(required = false, defaultValue = "asc") String direction,
                                     @RequestParam(required = false, defaultValue = "branchName") String sortBy,
                                     @RequestParam(required = false) String branchID,
                                     @RequestParam(required = false) String branchName,
                                     @RequestParam(required = false) String address,
                                     @RequestParam(required = false) String timeZone,
                                     @RequestParam(required = false) String branchWorkingHoursID,
                                     @RequestParam(required = false) String cityID
    ) {
        direction = PagingUtil.validateDirection(direction);

        SearchMerchantBranchRequest request = SearchMerchantBranchRequest.builder()
                .direction(direction)
                .sortBy(sortBy)
                .branchID(branchID)
                .merchantID(merchantID)
                .branchName(branchName)
                .address(address)
                .timezone(timeZone)
                .branchWorkingHoursID(branchWorkingHoursID)
                .cityID(cityID)
                .build();

        List<MerchantBranchResponse> merchantBranches = merchantBranchService.getAllActiveByMerchantId(request);

        CommonResponse<List<MerchantBranchResponse>> response = CommonResponse.<List<MerchantBranchResponse>>builder()
                .message("successfully get all active merchant branch")
                .statusCode(HttpStatus.OK.value())
                .data(merchantBranches)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }


    @PutMapping
    public ResponseEntity<?> update(@RequestBody UpdateMerchantBranchRequest request) {
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
    public ResponseEntity<?> delete(@PathVariable String id) {
        merchantBranchService.deleteById(id);
        CommonResponse<?> response = CommonResponse.builder()
                .message("successfully delete merchant branch")
                .statusCode(HttpStatus.OK.value())
                .data("OK")
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
