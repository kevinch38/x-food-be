package com.enigma.x_food.feature.user.dto.response;

import com.enigma.x_food.constant.ERole;
import com.enigma.x_food.feature.loyalty_point.dto.response.LoyaltyPointResponse;
import com.enigma.x_food.feature.voucher.dto.response.VoucherResponse;
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
public class UserResponse {
    private String accountID;
    private String ktpID;
    private String accountEmail;
    private String phoneNumber;
    private String pinID;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Timestamp createdAt;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String role;
    private byte[] profilePhoto;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Timestamp updatedAt;
    private String balanceID;
    private LoyaltyPointResponse loyaltyPoint;
    private String otpID;
    private List<VoucherResponse> vouchers;
}
