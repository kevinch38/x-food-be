package com.enigma.x_food.feature.payment;

import com.enigma.x_food.feature.payment.dto.request.SearchPaymentRequest;
import com.enigma.x_food.feature.payment.dto.request.PaymentRequest;
import com.enigma.x_food.feature.payment.dto.response.PaymentResponse;

import java.util.List;

public interface PaymentService {
    PaymentResponse createNew(PaymentRequest request);
    List<PaymentResponse> findByAccountId(SearchPaymentRequest request);
}