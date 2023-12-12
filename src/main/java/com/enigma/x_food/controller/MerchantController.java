package com.enigma.x_food.controller;

import com.enigma.x_food.feature.merchant.dto.request.NewMerchantRequest;
import com.enigma.x_food.feature.merchant.dto.response.MerchantResponse;
import com.enigma.x_food.feature.merchant.service.MerchantService;
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
@RequestMapping("api/merchants")
@RequiredArgsConstructor
public class MerchantController {
    private final MerchantService merchantService;
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNewMerchant(@RequestBody NewMerchantRequest request) {
        MerchantResponse merchantResponse = merchantService.createNew(request);
        CommonResponse<MerchantResponse> response = CommonResponse.<MerchantResponse>builder()
                .message("successfully create new merchant")
                .statusCode(HttpStatus.CREATED.value())
                .data(merchantResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
