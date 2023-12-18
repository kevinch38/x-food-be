package com.enigma.x_food.feature.top_up;

import com.enigma.x_food.feature.top_up.dto.request.TopUpRequest;
import com.enigma.x_food.feature.top_up.dto.response.TopUpResponse;
import com.enigma.x_food.shared.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/top-up")
@RequiredArgsConstructor
public class TopUpController {
    private final TopUpService topUpService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNew(@RequestBody TopUpRequest request) {
        TopUpResponse topUpResponse = topUpService.createNew(request);
        CommonResponse<TopUpResponse> response = CommonResponse.<TopUpResponse>builder()
                .message("successfully top up")
                .statusCode(HttpStatus.CREATED.value())
                .data(topUpResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
