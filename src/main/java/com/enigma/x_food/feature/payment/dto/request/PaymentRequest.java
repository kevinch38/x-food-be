package com.enigma.x_food.feature.payment.dto.request;

import com.enigma.x_food.constant.EPaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequest {
    @NotBlank(message = "Account ID cannot be empty")
    private String accountID;
    @NotNull(message = "Top up amount cannot be empty")
    private Double paymentAmount;
    @NotBlank(message = "Payment type cannot be empty")
    private String paymentType;
    @NotNull(message = "Expired at cannot be empty")
    private Timestamp expiredAt;
    @NotBlank(message = "Payment status ID cannot be empty")
    private String paymentStatusID;
    @NotBlank(message = "History ID cannot be empty")
    private String historyID;
    private String friendID;
    @NotBlank(message = "Order ID cannot be empty")
    private String orderID;
}
