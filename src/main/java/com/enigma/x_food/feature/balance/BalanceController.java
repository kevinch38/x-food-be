package com.enigma.x_food.feature.balance;

import com.enigma.x_food.feature.balance.dto.request.NewBalanceRequest;
import com.enigma.x_food.feature.balance.dto.request.UpdateBalanceRequest;
import com.enigma.x_food.feature.balance.dto.response.BalanceResponse;
import com.enigma.x_food.shared.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/balances")
@RequiredArgsConstructor
public class BalanceController {
    private final BalanceService balanceService;

//    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> createNew(@RequestBody NewBalanceRequest request) {
//        Balance balance = balanceService.createNew(request);
//        CommonResponse<Balance> response = CommonResponse.<Balance>builder()
//                .message("successfully create new balance")
//                .statusCode(HttpStatus.CREATED.value())
//                .data(balance)
//                .build();
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(response);
//    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody UpdateBalanceRequest request) {
        BalanceResponse balanceResponse = balanceService.update(request);
        CommonResponse<BalanceResponse> response = CommonResponse.<BalanceResponse>builder()
                .message("successfully update balance")
                .statusCode(HttpStatus.OK.value())
                .data(balanceResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        BalanceResponse balanceResponse = balanceService.findById(id);
        CommonResponse<BalanceResponse> response = CommonResponse.<BalanceResponse>builder()
                .message("successfully get balance")
                .statusCode(HttpStatus.OK.value())
                .data(balanceResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
