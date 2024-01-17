package com.enigma.x_food.feature.payment.dto.response;

import com.enigma.x_food.feature.friend.dto.response.FriendResponse;
import com.enigma.x_food.feature.order.dto.response.OrderResponse;
import com.enigma.x_food.feature.order_item.dto.response.OrderItemResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponse {
    private String paymentID;
    private String accountID;
    private Double paymentAmount;
    private String paymentType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Timestamp expiredAt;
    private String paymentStatus;
    private String historyID;
    private FriendResponse friend;
    private String orderID;
    private OrderResponse order;
    private List<OrderItemResponse> orderItems;
    private List<String> orderItemSplits;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Timestamp createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Timestamp updatedAt;
}
