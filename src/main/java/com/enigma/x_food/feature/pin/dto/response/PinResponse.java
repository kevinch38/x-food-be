package com.enigma.x_food.feature.pin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PinResponse {
    private String pinID;
    private String pin;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String accountID;
}
