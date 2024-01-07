package com.enigma.x_food.feature.otp;

import com.enigma.x_food.feature.otp.dto.request.CheckOTPRequest;
import com.enigma.x_food.security.BCryptUtil;
import com.enigma.x_food.util.ValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class OTPServiceImplTest {

    @MockBean
    private OTPRepository otpRepository;

    @MockBean
    private BCryptUtil bCryptUtil;

    @MockBean
    private ValidationUtil validationUtil;

    @Autowired
    private OTPServiceImpl otpService;

    @Test
    void createNew() {
        String newOtp = "1234";
        Mockito.when(bCryptUtil.hash(newOtp)).thenReturn("hashedOtp");
        Mockito.doNothing().when(validationUtil).validate(newOtp);

        OTP expectedOTP = OTP.builder()
                .otp("hashedOtp")
                .build();

        Mockito.when(otpRepository.saveAndFlush(Mockito.any(OTP.class))).thenReturn(expectedOTP);

        OTP actualOTP = otpService.createNew(newOtp);

        Assertions.assertEquals(expectedOTP, actualOTP);
    }

    @Test
    void createNewFailed() {
        String newOtp = "1234";
        Mockito.when(bCryptUtil.hash(newOtp)).thenReturn("hashedOtp");
        Mockito.doThrow(new DataIntegrityViolationException("")).when(otpRepository).saveAndFlush(Mockito.any(OTP.class));

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            otpService.createNew(newOtp);
        });
    }

    @Test
    void checkOtp() {
        String otpId = "123";
        String enteredOtp = "1234";

        CheckOTPRequest request = new CheckOTPRequest();
        request.setOtpID(otpId);
        request.setEnteredOtp(enteredOtp);

        OTP existingOTP = OTP.builder()
                .otp("hashedOtp")
                .build();

        Mockito.when(otpRepository.findById(otpId)).thenReturn(Optional.of(existingOTP));
        Mockito.when(bCryptUtil.check(enteredOtp, existingOTP.getOtp())).thenReturn(true);


    }

    @Test
    void checkOtpInvalid() {
        String otpId = "123";
        String enteredOtp = "5678";

        CheckOTPRequest request = new CheckOTPRequest();
        request.setOtpID(otpId);
        request.setEnteredOtp(enteredOtp);

        OTP existingOTP = OTP.builder()
                .otp("hashedOtp")
                .build();

        Mockito.when(otpRepository.findById(otpId)).thenReturn(Optional.of(existingOTP));
        Mockito.when(bCryptUtil.check(enteredOtp, existingOTP.getOtp())).thenReturn(false);


    }
}
