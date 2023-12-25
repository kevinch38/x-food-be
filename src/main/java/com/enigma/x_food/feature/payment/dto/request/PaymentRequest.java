package com.enigma.x_food.feature.payment.dto.request;

import com.enigma.x_food.feature.history.History;
import com.enigma.x_food.feature.order.Order;
import com.enigma.x_food.feature.user.User;
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
    @NotNull(message = "User cannot be empty")
    private User user;
    @NotNull(message = "Payment amount cannot be empty")
    private Double paymentAmount;
    @NotNull(message = "Expired at cannot be empty")
    private Timestamp expiredAt;
    @NotNull(message = "History cannot be empty")
    private History history;
    @NotNull(message = "Order cannot be empty")
    private Order order;
}
