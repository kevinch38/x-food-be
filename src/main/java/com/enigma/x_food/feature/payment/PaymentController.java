package com.enigma.x_food.feature.payment;

import com.enigma.x_food.feature.payment.dto.request.SearchPaymentRequest;
import com.enigma.x_food.feature.payment.dto.request.PaymentRequest;
import com.enigma.x_food.feature.payment.dto.response.PaymentResponse;
import com.enigma.x_food.shared.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNew(@RequestBody PaymentRequest request) {
        PaymentResponse paymentResponse = paymentService.createNew(request);
        CommonResponse<PaymentResponse> response = CommonResponse.<PaymentResponse>builder()
                .message("successfully create payment")
                .statusCode(HttpStatus.CREATED.value())
                .data(paymentResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{accountID}")
    public ResponseEntity<?> getAll(@PathVariable String accountID) {
        SearchPaymentRequest request = SearchPaymentRequest.builder()
                .accountID(accountID)
                .build();
        List<PaymentResponse> paymentResponses = paymentService.findByAccountId(request);

        CommonResponse<List<PaymentResponse>> response = CommonResponse.<List<PaymentResponse>>builder()
                .message("successfully get all history")
                .statusCode(HttpStatus.OK.value())
                .data(paymentResponses)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
