package com.enigma.x_food.feature.history;

import com.enigma.x_food.feature.history.dto.request.SearchHistoryRequest;
import com.enigma.x_food.feature.history.dto.response.HistoryResponse;
import com.enigma.x_food.shared.CommonResponse;
import com.enigma.x_food.shared.PagingResponse;
import com.enigma.x_food.util.PagingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/histories")
@RequiredArgsConstructor
public class HistoryController {
    private final HistoryService historyService;

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(required = false, defaultValue = "historyID") String sortBy,
            @RequestParam(required = false) String accountID,
            @RequestParam(required = false) String transactionType,
            @RequestParam(required = false) LocalDate startTransactionDate,
            @RequestParam(required = false) LocalDate endTransactionDate,
            @RequestParam(required = false, defaultValue = "false") Boolean paging
                                    ) {
        page = PagingUtil.validatePage(page);
        size = PagingUtil.validateSize(size);
        direction = PagingUtil.validateDirection(direction);

        SearchHistoryRequest request = SearchHistoryRequest.builder()
                .page(page)
                .size(size)
                .direction(direction)
                .sortBy(sortBy)
                .accountID(accountID)
                .transactionType(transactionType)
                .startTransactionDate(startTransactionDate)
                .endTransactionDate(endTransactionDate)
                .build();
        if  (paging){
            Page<HistoryResponse> histories = historyService.findAll(request);

            PagingResponse pagingResponse = PagingResponse.builder()
                    .page(page)
                    .size(size)
                    .count(histories.getTotalElements())
                    .totalPages(histories.getTotalPages())
                    .build();

            CommonResponse<List<HistoryResponse>> response = CommonResponse.<List<HistoryResponse>>builder()
                    .message("successfully get all histories")
                    .statusCode(HttpStatus.OK.value())
                    .data(histories.getContent())
                    .paging(pagingResponse)
                    .build();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(response);
        }
        else {
            List<HistoryResponse> histories = historyService.findByAccountId(request);

            CommonResponse<List<HistoryResponse>> response = CommonResponse.<List<HistoryResponse>>builder()
                    .message("successfully get all history by account id")
                    .statusCode(HttpStatus.OK.value())
                    .data(histories)
                    .build();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(response);

        }
    }
}
