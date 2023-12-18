package com.enigma.x_food.feature.history;

import com.enigma.x_food.feature.history.dto.request.SearchHistoryRequest;
import com.enigma.x_food.feature.history.dto.response.HistoryResponse;
import com.enigma.x_food.shared.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/histories")
@RequiredArgsConstructor
public class HistoryController {
    private final HistoryService historyService;

    @GetMapping("/{accountID}")
    public ResponseEntity<?> getAll(@PathVariable String accountID) {
        SearchHistoryRequest request = SearchHistoryRequest.builder()
                .accountID(accountID)
                .build();
        List<HistoryResponse> histories = historyService.findByAccountId(request);

        CommonResponse<List<HistoryResponse>> response = CommonResponse.<List<HistoryResponse>>builder()
                .message("successfully get all history")
                .statusCode(HttpStatus.OK.value())
                .data(histories)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
