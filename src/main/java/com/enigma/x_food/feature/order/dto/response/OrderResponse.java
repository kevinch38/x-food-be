package com.enigma.x_food.feature.order.dto.response;

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
public class OrderResponse {
    private String orderID;
    private String accountID;
    private String historyID;
    private Double orderValue;
    private String notes;
    private Integer tableNumber;
    private String orderStatus;
    private String branchID;
    private String merchantName;
    private byte[] image;
    private long quantity;
    private Boolean isSplit;
    private Integer pointAmount;
    private List<OrderItemResponse> orderItems;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Timestamp createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Timestamp updatedAt;
}
