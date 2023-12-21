package com.enigma.x_food.feature.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequest {
    @NotBlank(message = "ID is required")
    private String accountID;
    @NotNull(message = "KTP ID is required")
    private String ktpID;
    @NotNull(message = "Email is required")
    private String accountEmail;
    @NotNull(message = "First name is required")
    private String firstName;
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;
    @NotNull(message = "Last name is required")
    private String lastName;
    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;
}
