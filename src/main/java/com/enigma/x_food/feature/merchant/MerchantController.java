package com.enigma.x_food.feature.merchant;

import com.enigma.x_food.feature.merchant.dto.request.NewMerchantRequest;
import com.enigma.x_food.feature.merchant.dto.request.SearchActiveMerchantRequest;
import com.enigma.x_food.feature.merchant.dto.request.SearchMerchantRequest;
import com.enigma.x_food.feature.merchant.dto.request.UpdateMerchantRequest;
import com.enigma.x_food.feature.merchant.dto.response.MerchantResponse;
import com.enigma.x_food.shared.CommonResponse;
import com.enigma.x_food.shared.PagingResponse;
import com.enigma.x_food.util.PagingUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/merchants")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class MerchantController {
    private final MerchantService merchantService;

    @PreAuthorize("hasRole('PARTNERSHIP_STAFF')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNew(
            @RequestParam String merchantName,
            @RequestParam String picName,
            @RequestParam String picNumber,
            @RequestParam String picEmail,
            @RequestParam String merchantDescription,
            @RequestParam MultipartFile image,
            @RequestParam MultipartFile logoImage
    ) throws IOException, AuthenticationException {
        NewMerchantRequest request = NewMerchantRequest.builder()
                .merchantDescription(merchantDescription)
                .merchantName(merchantName)
                .picName(picName)
                .picEmail(picEmail)
                .picNumber(picNumber)
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

    @PreAuthorize("hasAnyRole('PARTNERSHIP_STAFF','SUPER_ADMIN', 'PARTNERSHIP_HEAD', 'MARKETING_STAFF', 'MARKETING_HEAD')")
    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(required = false, defaultValue = "merchantID") String sortBy,
            @RequestParam(required = false) String merchantName,
            @RequestParam(required = false) String merchantStatus,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startCreatedAt,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endCreatedAt,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startUpdatedAt,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endUpdatedAt,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startExpiredDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endExpiredDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startJoinDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endJoinDate
    ) {
        page = PagingUtil.validatePage(page);
        size = PagingUtil.validateSize(size);
        direction = PagingUtil.validateDirection(direction);

        SearchMerchantRequest request = SearchMerchantRequest.builder()
                .page(page)
                .size(size)
                .direction(direction)
                .sortBy(sortBy)
                .merchantName(merchantName)
                .merchantStatus(merchantStatus)
                .startCreatedAt(startCreatedAt)
                .endCreatedAt(endCreatedAt)
                .startUpdatedAt(startUpdatedAt)
                .endUpdatedAt(endUpdatedAt)
                .startExpiredDate(startExpiredDate)
                .endExpiredDate(endExpiredDate)
                .startJoinDate(startJoinDate)
                .endJoinDate(endJoinDate)
                .build();

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

    @PreAuthorize("permitAll")
    @GetMapping("/active")
    public ResponseEntity<?> getAllActive(
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(required = false, defaultValue = "merchantID") String sortBy,
            @RequestParam(required = false) String merchantName
    ) {
        direction = PagingUtil.validateDirection(direction);

        SearchActiveMerchantRequest request = SearchActiveMerchantRequest.builder()
                .direction(direction)
                .sortBy(sortBy)
                .merchantName(merchantName)
                .build();

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

    @PreAuthorize("permitAll")
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

    @PreAuthorize("hasRole('PARTNERSHIP_STAFF')")
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(
            @RequestParam String merchantID,
            @RequestParam String merchantName,
            @RequestParam String picName,
            @RequestParam String picNumber,
            @RequestParam String picEmail,
            @RequestParam String merchantDescription,
            @RequestParam MultipartFile image,
            @RequestParam MultipartFile logoImage
    ) throws IOException, AuthenticationException {
        UpdateMerchantRequest request = UpdateMerchantRequest.builder()
                .merchantID(merchantID)
                .merchantDescription(merchantDescription)
                .merchantName(merchantName)
                .picName(picName)
                .picEmail(picEmail)
                .picNumber(picNumber)
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

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'PARTNERSHIP_HEAD')")
    @PutMapping("/approve/active/{id}")
    public ResponseEntity<?> approveToActive(@PathVariable String id)  {
        merchantService.approveToActive(id);

        CommonResponse<String> response = CommonResponse.<String>builder()
                .message("successfully update merchant to active")
                .statusCode(HttpStatus.OK.value())
                .data("OK")
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'PARTNERSHIP_HEAD')")
    @PutMapping("/approve/inactive/{id}")
    public ResponseEntity<?> approveToInactive(@PathVariable String id)  {
        merchantService.deleteApprove(id);

        CommonResponse<String> response = CommonResponse.<String>builder()
                .message("successfully update merchant to inactive")
                .statusCode(HttpStatus.OK.value())
                .data("OK")
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PreAuthorize("hasAnyRole('PARTNERSHIP_STAFF')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMerchantById(@PathVariable String id) throws AuthenticationException {
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
