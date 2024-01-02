package com.enigma.x_food.feature.balance;

import com.enigma.x_food.feature.balance.dto.response.BalanceResponse;
import com.enigma.x_food.shared.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/balances")
@RequiredArgsConstructor
public class BalanceController {
    private final BalanceService balanceService;

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
