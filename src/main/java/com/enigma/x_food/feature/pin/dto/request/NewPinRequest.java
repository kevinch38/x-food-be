package com.enigma.x_food.feature.pin.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewPinRequest {
    @NotBlank(message = "Pin cannot be empty")
    private String pin;
    @NotBlank(message = "Account ID cannot be empty")
    private String accountID;
}
