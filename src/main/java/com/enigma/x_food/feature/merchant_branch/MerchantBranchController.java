package com.enigma.x_food.feature.merchant_branch;

import com.enigma.x_food.feature.merchant_branch.dto.request.NewMerchantBranchRequest;
import com.enigma.x_food.feature.merchant_branch.dto.response.MerchantBranchResponse;
import com.enigma.x_food.feature.pin.dto.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
