package com.enigma.x_food.feature.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewUserRequest {
    private String accountEmail;
    private String phoneNumber;
    private String pinID;
    private String firstName;
    private String lastName;
    private String balanceID;
    private String loyaltyPointID;
}
