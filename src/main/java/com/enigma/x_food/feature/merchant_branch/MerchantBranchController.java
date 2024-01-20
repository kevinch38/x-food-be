package com.enigma.x_food.feature.merchant_branch;

import com.enigma.x_food.feature.merchant_branch.dto.request.*;
import com.enigma.x_food.feature.merchant_branch.dto.response.MerchantBranchResponse;
import com.enigma.x_food.shared.CommonResponse;
import com.enigma.x_food.util.PagingUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
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
@RequestMapping("api/merchants/branches")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class MerchantBranchController {
    private final MerchantBranchService merchantBranchService;

    @PreAuthorize("hasRole('PARTNERSHIP_STAFF')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNew(@RequestBody NewMerchantBranchRequest request) throws AuthenticationException, IOException {
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

    @PreAuthorize("hasRole('PARTNERSHIP_STAFF')")
    @PutMapping(value = "/{merchantBranchID}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateImage(@RequestParam MultipartFile image,
                                         @PathVariable String merchantBranchID) throws IOException {

        UpdateImageMerchantBranchRequest request = UpdateImageMerchantBranchRequest.builder()
                .merchantBranchID(merchantBranchID)
                .image(image)
                .build();

        MerchantBranchResponse merchantBranchResponse = merchantBranchService.updateImage(request);
        CommonResponse<MerchantBranchResponse> response = CommonResponse.<MerchantBranchResponse>builder()
                .message("successfully create new merchant branch")
                .statusCode(HttpStatus.CREATED.value())
                .data(merchantBranchResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PreAuthorize("hasAnyRole('PARTNERSHIP_STAFF','SUPER_ADMIN', 'PARTNERSHIP_HEAD', 'MARKETING_STAFF', 'MARKETING_HEAD')")
    @GetMapping
    public ResponseEntity<?> findAll(@RequestParam String merchantID,
                                     @RequestParam(required = false, defaultValue = "asc") String direction,
                                     @RequestParam(required = false, defaultValue = "branchID") String sortBy,
                                     @RequestParam(required = false, defaultValue = "1") Integer page,
                                     @RequestParam(required = false, defaultValue = "10") Integer size,
                                     @RequestParam(required = false) String branchName,
                                     @RequestParam(required = false) String city,
                                     @RequestParam(required = false) String status,
                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startJoinDate,
                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endJoinDate
    ) {
        page = PagingUtil.validatePage(page);
        size = PagingUtil.validateSize(size);
        direction = PagingUtil.validateDirection(direction);

        SearchMerchantBranchRequest request = SearchMerchantBranchRequest.builder()
                .direction(direction)
                .sortBy(sortBy)
                .page(page)
                .size(size)
                .merchantID(merchantID)
                .branchName(branchName)
                .city(city)
                .status(status)
                .startJoinDate(startJoinDate)
                .endJoinDate(endJoinDate)
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

    @PreAuthorize("permitAll")
    @GetMapping("/active")
    public ResponseEntity<?> findAllActive(@RequestParam String merchantID,
                                           @RequestParam(required = false, defaultValue = "asc") String direction,
                                           @RequestParam(required = false, defaultValue = "branchID") String sortBy,
                                           @RequestParam(required = false) String branchName,
                                           @RequestParam(required = false) String city,
                                           @RequestParam(required = false) String status
    ) {
        direction = PagingUtil.validateDirection(direction);

        SearchActiveMerchantBranchRequest request = SearchActiveMerchantBranchRequest.builder()
                .direction(direction)
                .sortBy(sortBy)
                .merchantID(merchantID)
                .branchName(branchName)
                .city(city)
                .status(status)
                .build();

        List<MerchantBranchResponse> merchantBranches = merchantBranchService.findAllActiveByMerchantId(request);

        CommonResponse<List<MerchantBranchResponse>> response = CommonResponse.<List<MerchantBranchResponse>>builder()
                .message("successfully get all active merchant branch")
                .statusCode(HttpStatus.OK.value())
                .data(merchantBranches)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{branchID}")
    public ResponseEntity<?> findById(@PathVariable String branchID) {
        MerchantBranchResponse merchantBranchResponse = merchantBranchService.findById(branchID);
        CommonResponse<MerchantBranchResponse> response = CommonResponse.<MerchantBranchResponse>builder()
                .message("successfully get merchant branch")
                .statusCode(HttpStatus.OK.value())
                .data(merchantBranchResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PreAuthorize("hasRole('PARTNERSHIP_STAFF')")
    @PutMapping
    public ResponseEntity<?> update(@RequestBody UpdateMerchantBranchRequest request) throws IOException, AuthenticationException {
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

    @PreAuthorize("hasRole('PARTNERSHIP_HEAD')")
    @PutMapping("/approve/active/{id}")
    public ResponseEntity<?> approveToActive(@PathVariable String id) throws AuthenticationException {
        merchantBranchService.approveToActive(id);

        CommonResponse<String> response = CommonResponse.<String>builder()
                .message("successfully update merchant to active")
                .statusCode(HttpStatus.OK.value())
                .data("OK")
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PreAuthorize("hasRole('PARTNERSHIP_HEAD')")
    @PutMapping("/approve/inactive/{id}")
    public ResponseEntity<?> approveToInactive(@PathVariable String id) throws AuthenticationException {
        merchantBranchService.deleteApprove(id);

        CommonResponse<String> response = CommonResponse.<String>builder()
                .message("successfully update merchant to inactive")
                .statusCode(HttpStatus.OK.value())
                .data("OK")
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PreAuthorize("hasRole('PARTNERSHIP_HEAD')")
    @PutMapping("/reject/update/{id}")
    public ResponseEntity<?> rejectUpdate(@PathVariable String id) throws AuthenticationException {
        merchantBranchService.rejectUpdate(id);

        CommonResponse<String> response = CommonResponse.<String>builder()
                .message("successfully reject merchant update")
                .statusCode(HttpStatus.OK.value())
                .data("OK")
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PreAuthorize("hasRole('PARTNERSHIP_STAFF')")
    @DeleteMapping("/{branchID}")
    public ResponseEntity<?> delete(@PathVariable String branchID) throws AuthenticationException {
        merchantBranchService.deleteById(branchID);
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

