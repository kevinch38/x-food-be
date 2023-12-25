package com.enigma.x_food.feature.order;

import com.enigma.x_food.feature.order.dto.request.OrderRequest;
import com.enigma.x_food.feature.order.dto.request.SearchOrderRequest;
import com.enigma.x_food.feature.order.dto.response.OrderResponse;
import com.enigma.x_food.shared.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNew(@RequestBody OrderRequest request) {
        OrderResponse orderResponse = orderService.createNew(request);
        CommonResponse<OrderResponse> response = CommonResponse.<OrderResponse>builder()
                .message("successfully create order")
                .statusCode(HttpStatus.CREATED.value())
                .data(orderResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam String accountID) {
        SearchOrderRequest request = SearchOrderRequest.builder()
                .accountID(accountID)
                .build();
        List<OrderResponse> orderResponses = orderService.findByAccountId(request);

        CommonResponse<List<OrderResponse>> response = CommonResponse.<List<OrderResponse>>builder()
                .message("successfully get all history")
                .statusCode(HttpStatus.OK.value())
                .data(orderResponses)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
