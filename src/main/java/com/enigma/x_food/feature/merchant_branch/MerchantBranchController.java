package com.enigma.x_food.feature.merchant_branch;

import com.enigma.x_food.feature.merchant_branch.dto.request.NewMerchantBranchRequest;
import com.enigma.x_food.feature.merchant_branch.dto.request.SearchActiveMerchantBranchRequest;
import com.enigma.x_food.feature.merchant_branch.dto.request.UpdateMerchantBranchRequest;
import com.enigma.x_food.feature.merchant_branch.dto.response.MerchantBranchResponse;
import com.enigma.x_food.feature.merchant_branch.dto.request.SearchMerchantBranchRequest;
import com.enigma.x_food.shared.CommonResponse;
import com.enigma.x_food.util.PagingUtil;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("api/merchants/branches")
@RequiredArgsConstructor
public class MerchantBranchController {
    private final MerchantBranchService merchantBranchService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNew(
            @RequestParam String merchantID,
            @RequestParam String branchName,
            @RequestParam String address,
            @RequestParam String timezone,
            @RequestParam String branchWorkingHoursID,
            @RequestParam String cityID,
            @RequestParam String picName,
            @RequestParam String picNumber,
            @RequestParam String picEmail,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String joinDate,
            @RequestParam MultipartFile image
    ) throws IOException, AuthenticationException {
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.parse(joinDate, DateTimeFormatter.ISO_DATE_TIME));

        NewMerchantBranchRequest request = NewMerchantBranchRequest.builder()
                .merchantID(merchantID)
                .branchName(branchName)
                .address(address)
                .timezone(timezone)
                .branchWorkingHoursID(branchWorkingHoursID)
                .cityID(cityID)
                .image(image)
                .picName(picName)
                .picNumber(picNumber)
                .picEmail(picEmail)
                .joinDate(timestamp)
                .build();

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
    public ResponseEntity<?> findAll(@RequestParam String merchantID,
                                     @RequestParam(required = false, defaultValue = "asc") String direction,
                                     @RequestParam(required = false, defaultValue = "branchID") String sortBy,
                                     @RequestParam(required = false, defaultValue = "1") Integer page,
                                     @RequestParam(required = false, defaultValue = "10") Integer size,
                                     @RequestParam(required = false) String branchName,
                                     @RequestParam(required = false) String city,
                                     @RequestParam(required = false) String status,
                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startJoinDate,
                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endJoinDate
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

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(
            @RequestParam String branchID,
            @RequestParam String branchName,
            @RequestParam String address,
            @RequestParam String timezone,
            @RequestParam String branchWorkingHoursID,
            @RequestParam String cityID,
            @RequestParam String picName,
            @RequestParam String picNumber,
            @RequestParam String picEmail,
            @RequestParam MultipartFile image
    ) throws IOException, AuthenticationException {
        UpdateMerchantBranchRequest request = UpdateMerchantBranchRequest.builder()
                .branchID(branchID)
                .branchName(branchName)
                .address(address)
                .timezone(timezone)
                .branchWorkingHoursID(branchWorkingHoursID)
                .cityID(cityID)
                .image(image)
                .picName(picName)
                .picNumber(picNumber)
                .picEmail(picEmail)
                .build();

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

