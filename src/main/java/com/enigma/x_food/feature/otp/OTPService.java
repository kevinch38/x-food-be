package com.enigma.x_food.feature.otp;

import com.enigma.x_food.feature.otp.dto.request.NewOTPRequest;
import com.enigma.x_food.feature.otp.dto.request.SearchOTPRequest;
import com.enigma.x_food.feature.otp.dto.response.OTPResponse;
import org.springframework.data.domain.Page;

public interface OTPService {
    OTPResponse createNew(NewOTPRequest request);
//    Page<OTPResponse> getAll(SearchOTPRequest request);
}
