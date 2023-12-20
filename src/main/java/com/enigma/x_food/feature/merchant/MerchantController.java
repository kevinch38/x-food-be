package com.enigma.x_food.feature.merchant;

import com.enigma.x_food.feature.merchant.dto.request.NewMerchantRequest;
import com.enigma.x_food.feature.merchant.dto.request.SearchMerchantRequest;
import com.enigma.x_food.feature.merchant.dto.request.UpdateMerchantRequest;
import com.enigma.x_food.feature.merchant.dto.response.MerchantResponse;
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
    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(required = false, defaultValue = "merchantID") String sortBy,
            @RequestParam(required = false) String merchantID,
            @RequestParam(required = false) String merchantName,
            @RequestParam(required = false) String picName,
            @RequestParam(required = false) String picNumber,
            @RequestParam(required = false) String picEmail,
            @RequestParam(required = false) String merchantDescription,
            @RequestParam(required = false) String adminID,
            @RequestParam(required = false) String merchantStatusID,
            @RequestParam(required = false) String notes
    ) {
        page = PagingUtil.validatePage(page);
        size = PagingUtil.validateSize(size);
        direction = PagingUtil.validateDirection(direction);

        SearchMerchantRequest request = SearchMerchantRequest.builder()
                .page(page)
                .size(size)
                .direction(direction)
                .sortBy(sortBy)
                .merchantID(merchantID)
                .merchantName(merchantName)
                .picName(picName)
                .picNumber(picNumber)
                .picEmail(picEmail)
                .merchantDescription(merchantDescription)
                .adminID(adminID)
                .merchantStatusID(merchantStatusID)
                .notes(notes)
                .build();

        Page<MerchantResponse> merchants = merchantService.getAll(request);

        PagingResponse pagingResponse = PagingResponse.builder()
                .page(page)
                .size(size)
                .count(merchants.getTotalElements())
                .totalPages(merchants.getTotalPages())
                .build();

        CommonResponse<List<MerchantResponse>> response = CommonResponse.<List<MerchantResponse>>builder()
                .message("successfully get all merchant")
                .statusCode(HttpStatus.OK.value())
                .data(merchants.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/active")
    public ResponseEntity<?> getAllActive(
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(required = false, defaultValue = "merchantID") String sortBy,
            @RequestParam(required = false) String merchantID,
            @RequestParam(required = false) String merchantName,
            @RequestParam(required = false) String picName,
            @RequestParam(required = false) String picNumber,
            @RequestParam(required = false) String picEmail,
            @RequestParam(required = false) String merchantDescription,
            @RequestParam(required = false) String adminID,
            @RequestParam(required = false) String merchantStatusID,
            @RequestParam(required = false) String notes
    ) {
        SearchMerchantRequest request = SearchMerchantRequest.builder()
                .direction(direction)
                .sortBy(sortBy)
                .merchantID(merchantID)
                .merchantName(merchantName)
                .picName(picName)
                .picNumber(picNumber)
                .picEmail(picEmail)
                .merchantDescription(merchantDescription)
                .adminID(adminID)
                .merchantStatusID(merchantStatusID)
                .notes(notes)
                .build();

        List<MerchantResponse> merchants = merchantService.getAllActive(request);

        CommonResponse<List<MerchantResponse>> response = CommonResponse.<List<MerchantResponse>>builder()
                .message("successfully get all active merchant")
                .statusCode(HttpStatus.OK.value())
                .data(merchants)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping
    public ResponseEntity<?> updateMerchant(@RequestBody UpdateMerchantRequest request) {
        MerchantResponse merchantResponse = merchantService.update(request);
        CommonResponse<MerchantResponse> response = CommonResponse.<MerchantResponse>builder()
                .message("successfully update merchant")
                .statusCode(HttpStatus.OK.value())
                .data(merchantResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMerchantById(@PathVariable String id) {
        merchantService.deleteById(id);
        CommonResponse<?> response = CommonResponse.builder()
                .message("successfully delete merchant")
                .statusCode(HttpStatus.OK.value())
                .data("OK")
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
