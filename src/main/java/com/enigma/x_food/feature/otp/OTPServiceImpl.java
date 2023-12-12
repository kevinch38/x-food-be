package com.enigma.x_food.feature.otp;

import com.enigma.x_food.feature.otp.dto.request.NewOTPRequest;
import com.enigma.x_food.feature.otp.dto.response.OTPResponse;
import com.enigma.x_food.security.BCryptUtil;
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
    @Transactional(rollbackFor = Exception.class)
    @Override
    public OTPResponse createNew(NewOTPRequest request) {
        try {
            log.info("Start createNew");
            validationUtil.validate(request);
            OTP otp = OTP.builder()
                    .otp(bCryptUtil.hash(request.getOtp()))
                    .accountID(request.getAccountID())
                    .build();
            otpRepository.saveAndFlush(otp);
            log.info("End createNew");
            return mapToResponse(otp);
        } catch (DataIntegrityViolationException e) {
            log.error("Error createNew: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "otp already exist");
        }
    }

//    @Override
//    @Transactional(readOnly = true)
//    public Page<OTPResponse> getAll(SearchOTPRequest request) {
//        log.info("Start getAll");
//        String fieldName = SortingUtil.sortByValidation(OTP.class, request.getSortBy(), "otpID");
//        request.setSortBy(fieldName);
//
//        Sort.Direction direction = Sort.Direction.fromString(request.getDirection());
//        Pageable pageable = PageRequest.of(
//                request.getPage() - 1,
//                request.getSize(),
//                direction,
//                request.getSortBy()
//        );
//
//        Page<OTP> otps = otpRepository.findAll(pageable);
//        log.info("End getAll");
//        return otps.map(this::mapToResponse);
//    }

    private OTPResponse mapToResponse(OTP otp) {
        return OTPResponse.builder()
                .otpID(otp.getOtpID())
                .otp(otp.getOtp())
                .accountID(otp.getAccountID())
                .createdAt(otp.getCreatedAt())
                .updatedAt(otp.getUpdatedAt())
                .build();
    }
}
