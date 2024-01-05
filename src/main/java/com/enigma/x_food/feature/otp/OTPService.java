package com.enigma.x_food.feature.otp;

import com.enigma.x_food.feature.otp.dto.request.CheckOTPRequest;
import com.enigma.x_food.feature.otp.dto.response.OTPResponse;
import com.enigma.x_food.feature.otp.dto.response.OTPTokenResponse;

public interface OTPService {
    OTP createNew(String otp);
    OTPResponse findById(String id);
    OTPTokenResponse checkOtp(CheckOTPRequest request);
}
