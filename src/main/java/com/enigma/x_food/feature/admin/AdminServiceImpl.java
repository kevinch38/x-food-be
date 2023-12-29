package com.enigma.x_food.feature.admin;

import com.enigma.x_food.feature.admin.dto.request.NewAdminRequest;
import com.enigma.x_food.feature.admin.dto.request.UpdateAdminRequest;
import com.enigma.x_food.feature.admin.dto.response.AdminResponse;
import com.enigma.x_food.feature.balance.Balance;
import com.enigma.x_food.feature.balance.BalanceService;
import com.enigma.x_food.feature.balance.dto.request.NewBalanceRequest;
import com.enigma.x_food.feature.loyalty_point.LoyaltyPoint;
import com.enigma.x_food.feature.loyalty_point.LoyaltyPointService;
import com.enigma.x_food.feature.loyalty_point.dto.request.NewLoyaltyPointRequest;
import com.enigma.x_food.feature.otp.OTP;
import com.enigma.x_food.feature.otp.OTPService;
import com.enigma.x_food.feature.pin.Pin;
import com.enigma.x_food.feature.pin.PinService;
import com.enigma.x_food.feature.pin.dto.request.NewPinRequest;
import com.enigma.x_food.security.BCryptUtil;
import com.enigma.x_food.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final PinService pinService;
    private final OTPService otpService;
    private final BalanceService balanceService;
    private final LoyaltyPointService loyaltyPointService;
    private final ValidationUtil validationUtil;
    private final Random random;
    private final BCryptUtil bCryptUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AdminResponse createNew(NewAdminRequest request) {
        try {
            log.info("Start createNew");
            validationUtil.validate(request);
            Pin pin = pinService.createNew(NewPinRequest.builder().pin("").build());
            OTP otp = otpService.createNew("1111");
            Balance balance = balanceService.createNew(NewBalanceRequest.builder()
                    .totalBalance(0D)
                    .build());

            LoyaltyPoint loyaltyPoint = loyaltyPointService.createNew(NewLoyaltyPointRequest.builder()
                    .loyaltyPointAmount(0)
                    .build());



           return null;
        } catch (DataIntegrityViolationException e) {
            log.error("Error createNew: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone number already exist");
        }
    }

    @Override
    public AdminResponse getById(String id) {
        log.info("Start getOneById");
        Admin admin = findByIdOrThrowNotFound(id);
        log.info("End getOneById");
        return mapToResponse(admin);
    }

    @Override
    public Admin getAdminById(String id) {
        return (findByIdOrThrowNotFound(id));
    }


    @Override
    public AdminResponse update(UpdateAdminRequest request) {
       return null;
    }

    private AdminResponse mapToResponse(Admin admin) {
        return null;
    }


    private Admin findByIdOrThrowNotFound(String id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "admin not found"));
    }
}
