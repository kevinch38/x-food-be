package com.enigma.x_food.feature.otp;

import com.enigma.x_food.feature.otp.dto.request.CheckOTPRequest;
import com.enigma.x_food.feature.otp.dto.response.OTPResponse;
import com.enigma.x_food.feature.otp.dto.response.OTPTokenResponse;
import com.enigma.x_food.security.BCryptUtil;
import com.enigma.x_food.security.JwtUtil;
import com.enigma.x_food.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class OTPServiceImpl implements OTPService {
    private final OTPRepository otpRepository;
    private final BCryptUtil bCryptUtil;
    private final ValidationUtil validationUtil;
    private final JwtUtil jwtUtil;
    @Transactional(rollbackFor = Exception.class)
    @Override
    public OTP createNew(String newOtp) {
        try {
            log.info("Start createNew");
            validationUtil.validate(newOtp);
            OTP otp = OTP.builder()
                    .otp(bCryptUtil.hash(newOtp))
                    .build();
            otpRepository.saveAndFlush(otp);
            log.info("End createNew");
            return otp;
        } catch (DataIntegrityViolationException e) {
            log.error("Error createNew: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "otp already exist");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public OTPTokenResponse checkOtp(CheckOTPRequest request) {
        validationUtil.validate(request);

        OTP otp = findByIdOrThrowException(request.getOtpID());

        if(bCryptUtil.check(request.getEnteredOtp(), otp.getOtp())){
            String token = jwtUtil.generateTokenUser(otp.getUser());
            return OTPTokenResponse.builder()
                    .check(true)
                    .token(token)
                    .build();
        }
        return OTPTokenResponse.builder()
                .check(false)
                .build();
    }
    @Override
    @Transactional(readOnly = true)
    public OTPResponse findById(String id) {
        return mapToResponse(findByIdOrThrowException(id),null);
    }

    private OTP findByIdOrThrowException(String id) {
        return otpRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "OTP not found"));
    }

    private OTPResponse mapToResponse(OTP otp,String token) {
        return OTPResponse.builder()
                .otpID(otp.getOtpID())
                .otp(otp.getOtp())
                .createdAt(otp.getCreatedAt())
                .updatedAt(otp.getUpdatedAt())
                .token(token)
                .build();
    }
}
