package com.enigma.x_food.feature.order.dto.request;

import com.enigma.x_food.feature.history.History;
import com.enigma.x_food.feature.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {
    @NotBlank(message = "Account ID cannot be empty")
    private String accountID;
    @NotBlank(message = "History ID cannot be empty")
    private String historyID;
    @NotBlank(message = "Order value cannot be empty")
    private Double orderValue;
    @NotBlank(message = "Notes cannot be empty")
    private String notes;
    @NotBlank(message = "Table number cannot be empty")
    private Integer tableNumber;
    @NotBlank(message = "Order status ID cannot be empty")
    private Double orderStatusID;
    @NotBlank(message = "Branch ID cannot be empty")
    private String branchID;
    @NotBlank(message = "Payment ID cannot be empty")
    private String paymentID;
}
