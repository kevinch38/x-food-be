package com.enigma.x_food.feature.voucher;

import com.enigma.x_food.feature.voucher.dto.request.NewVoucherRequest;
import com.enigma.x_food.feature.voucher.dto.request.SearchVoucherRequest;
import com.enigma.x_food.feature.voucher.dto.response.VoucherResponse;
import com.enigma.x_food.feature.voucher.dto.request.UpdateVoucherRequest;
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
@RequestMapping("/api/vouchers")
@RequiredArgsConstructor
public class VoucherController {
    private final VoucherService voucherService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNew(@RequestBody NewVoucherRequest request) {
        VoucherResponse voucherResponse = voucherService.createNew(request);
        CommonResponse<VoucherResponse> response = CommonResponse.<VoucherResponse>builder()
                .message("successfully create new voucher")
                .statusCode(HttpStatus.CREATED.value())
                .data(voucherResponse)
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
            @RequestParam(required = false, defaultValue = "voucherID") String sortBy,
            @RequestParam(required = false) String voucherID
    ) {
        page = PagingUtil.validatePage(page);
        size = PagingUtil.validateSize(size);
        direction = PagingUtil.validateDirection(direction);

        SearchVoucherRequest request = SearchVoucherRequest.builder()
                .page(page)
                .size(size)
                .direction(direction)
                .sortBy(sortBy)
                .voucherID(voucherID)
                .build();
        Page<VoucherResponse> vouchers = voucherService.getAll(request);

       PagingResponse pagingResponse = PagingResponse.builder()
                .page(page)
                .size(size)
                .count(vouchers.getTotalElements())
                .totalPages(vouchers.getTotalPages())
                .build();

        CommonResponse<List<VoucherResponse>> response = CommonResponse.<List<VoucherResponse>>builder()
                .message("successfully get all voucher")
                .statusCode(HttpStatus.OK.value())
                .data(vouchers.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@PathVariable String id) {
        VoucherResponse voucherResponse = voucherService.findById(id);
        CommonResponse<VoucherResponse> response = CommonResponse.<VoucherResponse>builder()
                .message("successfully get voucher")
                .statusCode(HttpStatus.OK.value())
                .data(voucherResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }


    @PutMapping
    public ResponseEntity<?> update(@RequestBody UpdateVoucherRequest request) {
        VoucherResponse voucherResponse = voucherService.update(request);
        CommonResponse<VoucherResponse> response = CommonResponse.<VoucherResponse>builder()
                .message("successfully update voucher")
                .statusCode(HttpStatus.OK.value())
                .data(voucherResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id) {
        voucherService.deleteById(id);
        CommonResponse<String> response = CommonResponse.<String>builder()
                .message("successfully delete voucher")
                .statusCode(HttpStatus.OK.value())
                .data("OK")
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

}
