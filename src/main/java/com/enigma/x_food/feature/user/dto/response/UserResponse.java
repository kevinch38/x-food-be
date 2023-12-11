package com.enigma.x_food.feature.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private String accountID;
    private String ktpID;
    private String accountEmail;
    private String phoneNumber;
    private String pinID;
    private Timestamp createdAt;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Timestamp updatedAt;
    private String balanceID;
    private String loyaltyPointID;
    private String otpID;
}
