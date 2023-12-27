package com.enigma.x_food.feature.loyalty_point;

import com.enigma.x_food.feature.loyalty_point.dto.request.UpdateLoyaltyPointRequest;
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

//    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> createNew(@RequestBody NewLoyaltyPointRequest request) {
//        LoyaltyPoint loyaltyPoint = loyaltyPointService.createNew(request);
//        CommonResponse<LoyaltyPoint> response = CommonResponse.<LoyaltyPoint>builder()
//                .message("successfully create new loyaltyPoint")
//                .statusCode(HttpStatus.CREATED.value())
//                .data(loyaltyPoint)
//                .build();
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(response);
//    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody UpdateLoyaltyPointRequest request) {
        LoyaltyPointResponse loyaltyPointResponse = loyaltyPointService.update(request);
        CommonResponse<LoyaltyPointResponse> response = CommonResponse.<LoyaltyPointResponse>builder()
                .message("successfully update loyalty point")
                .statusCode(HttpStatus.OK.value())
                .data(loyaltyPointResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        LoyaltyPoint loyaltyPointResponse = loyaltyPointService.findById(id);
        CommonResponse<LoyaltyPoint> response = CommonResponse.<LoyaltyPoint>builder()
                .message("successfully get loyalty point")
                .statusCode(HttpStatus.OK.value())
                .data(loyaltyPointResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
