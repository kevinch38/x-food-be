package com.enigma.x_food.feature.loyalty_point;

import com.enigma.x_food.feature.loyalty_point.dto.response.LoyaltyPointResponse;
import com.enigma.x_food.shared.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loyalty-points")
@RequiredArgsConstructor
public class LoyaltyPointController {
    private final LoyaltyPointService loyaltyPointService;
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        LoyaltyPointResponse loyaltyPointResponse = loyaltyPointService.findById(id);
        CommonResponse<LoyaltyPointResponse> response = CommonResponse.<LoyaltyPointResponse>builder()
                .message("successfully get loyalty point")
                .statusCode(HttpStatus.OK.value())
                .data(loyaltyPointResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
