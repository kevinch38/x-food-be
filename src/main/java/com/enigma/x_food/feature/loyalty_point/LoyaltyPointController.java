package com.enigma.x_food.feature.loyalty_point;

import com.enigma.x_food.feature.loyalty_point.dto.response.LoyaltyPointResponse;
import com.enigma.x_food.shared.CommonResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loyalty-points")
@RequiredArgsConstructor
@PreAuthorize("permitAll")
@SecurityRequirement(name = "Bearer Authentication")
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
