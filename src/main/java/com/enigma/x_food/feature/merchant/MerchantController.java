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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("api/merchants")
@RequiredArgsConstructor
public class MerchantController {
    private final MerchantService merchantService;
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNew(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String joinDate,
            @RequestParam String merchantName,
            @RequestParam String picName,
            @RequestParam String picNumber,
            @RequestParam String picEmail,
            @RequestParam String merchantDescription,
            @RequestParam String notes,
            @RequestParam MultipartFile image,
            @RequestParam MultipartFile logoImage
                                               ) throws IOException {
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.parse(joinDate, DateTimeFormatter.ISO_DATE_TIME));

        NewMerchantRequest request = NewMerchantRequest.builder()
                .joinDate(timestamp)
                .merchantDescription(merchantDescription)
                .merchantName(merchantName)
                .picName(picName)
                .picEmail(picEmail)
                .picNumber(picNumber)
                .notes(notes)
                .image(image)
                .logoImage(logoImage)
                .build();

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
            @RequestParam(required = false) String notes,
            @RequestParam(required = false, defaultValue = "false") Boolean paging
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

        if (paging) {
            Page<MerchantResponse> merchants = merchantService.getAll(request);

            PagingResponse pagingResponse = PagingResponse.builder()
                    .page(page)
                    .size(size)
                    .count(merchants.getTotalElements())
                    .totalPages(merchants.getTotalPages())
                    .build();

            CommonResponse<List<MerchantResponse>> response = CommonResponse.<List<MerchantResponse>>builder()
                    .message("successfully get all merchants")
                    .statusCode(HttpStatus.OK.value())
                    .data(merchants.getContent())
                    .paging(pagingResponse)
                    .build();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(response);
        }
        else {
            List<MerchantResponse> merchants = merchantService.getAllActive(request);

            CommonResponse<List<MerchantResponse>> response = CommonResponse.<List<MerchantResponse>>builder()
                    .message("successfully get all active merchants")
                    .statusCode(HttpStatus.OK.value())
                    .data(merchants)
                    .build();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(response);
        }
    }

    @GetMapping("/{merchantID}")
    public ResponseEntity<?> getById(@PathVariable String merchantID) {
        MerchantResponse merchantResponse = merchantService.findById(merchantID);
        CommonResponse<?> response = CommonResponse.builder()
                .message("successfully get merchant")
                .statusCode(HttpStatus.OK.value())
                .data(merchantResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(
            @RequestParam String merchantID,
            @RequestParam String merchantName,
            @RequestParam String picName,
            @RequestParam String picNumber,
            @RequestParam String picEmail,
            @RequestParam String merchantDescription,
            @RequestParam String notes,
            @RequestParam MultipartFile image,
            @RequestParam MultipartFile logoImage
    ) throws IOException {
        UpdateMerchantRequest request = UpdateMerchantRequest.builder()
                .merchantID(merchantID)
                .merchantDescription(merchantDescription)
                .merchantName(merchantName)
                .picName(picName)
                .picEmail(picEmail)
                .picNumber(picNumber)
                .notes(notes)
                .image(image)
                .logoImage(logoImage)
                .build();

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
