package com.enigma.x_food.feature.top_up;

import com.enigma.x_food.feature.top_up.dto.request.SearchTopUpRequest;
import com.enigma.x_food.feature.top_up.dto.request.TopUpRequest;
import com.enigma.x_food.feature.top_up.dto.response.TopUpResponse;
import com.enigma.x_food.shared.CommonResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/top-up")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class TopUpController {
    private final TopUpService topUpService;

    @PreAuthorize("hasRole('USER')")
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

    @PreAuthorize("permitAll")
    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam String accountID) {
        SearchTopUpRequest request = SearchTopUpRequest.builder()
                .accountID(accountID)
                .build();
        List<TopUpResponse> topUpResponses = topUpService.findByAccountId(request);

        CommonResponse<List<TopUpResponse>> response = CommonResponse.<List<TopUpResponse>>builder()
                .message("successfully get all top up history")
                .statusCode(HttpStatus.OK.value())
                .data(topUpResponses)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
