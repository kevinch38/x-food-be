package com.enigma.x_food.feature.payment_status;

import com.enigma.x_food.constant.EPaymentStatus;
import com.enigma.x_food.feature.payment_status.response.PaymentStatusResponse;

import java.util.List;

public interface PaymentStatusService {
    PaymentStatus getByStatus(EPaymentStatus status);
    List<PaymentStatusResponse> getAll();
}
