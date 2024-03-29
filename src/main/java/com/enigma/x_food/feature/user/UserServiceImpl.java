package com.enigma.x_food.feature.user;

import com.enigma.x_food.constant.ERole;
import com.enigma.x_food.constant.EVoucherStatus;
import com.enigma.x_food.feature.balance.Balance;
import com.enigma.x_food.feature.balance.BalanceService;
import com.enigma.x_food.feature.balance.dto.request.NewBalanceRequest;
import com.enigma.x_food.feature.balance.dto.response.BalanceResponse;
import com.enigma.x_food.feature.loyalty_point.LoyaltyPoint;
import com.enigma.x_food.feature.loyalty_point.LoyaltyPointService;
import com.enigma.x_food.feature.loyalty_point.dto.request.NewLoyaltyPointRequest;
import com.enigma.x_food.feature.loyalty_point.dto.response.LoyaltyPointResponse;
import com.enigma.x_food.feature.otp.OTP;
import com.enigma.x_food.feature.otp.OTPService;
import com.enigma.x_food.feature.pin.Pin;
import com.enigma.x_food.feature.pin.PinService;
import com.enigma.x_food.feature.pin.dto.request.NewPinRequest;
import com.enigma.x_food.feature.role.Role;
import com.enigma.x_food.feature.role.RoleService;
import com.enigma.x_food.feature.user.dto.request.NewUserRequest;
import com.enigma.x_food.feature.user.dto.request.UpdateUserProfilePhotoRequest;
import com.enigma.x_food.feature.user.dto.request.UpdateUserRequest;
import com.enigma.x_food.feature.user.dto.response.UserResponse;
import com.enigma.x_food.feature.user.dto.request.SearchUserRequest;
import com.enigma.x_food.feature.voucher.Voucher;
import com.enigma.x_food.feature.voucher.dto.response.VoucherResponse;
import com.enigma.x_food.security.BCryptUtil;
import com.enigma.x_food.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.enigma.x_food.util.SortingUtil;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.Predicate;
import java.sql.Timestamp;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PinService pinService;
    private final OTPService otpService;
    private final BalanceService balanceService;
    private final LoyaltyPointService loyaltyPointService;
    private final ValidationUtil validationUtil;
    private final RoleService roleService;
    private final Random random;
    private final BCryptUtil bCryptUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserResponse createNew(NewUserRequest request) {
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

            Role role = roleService.getByRole(ERole.ROLE_USER);
            User user = User.builder()
                    .ktpID("")
                    .accountEmail(bCryptUtil.hash(String.valueOf(random.nextInt())))
                    .pin(pin)
                    .phoneNumber("+62" + request.getPhoneNumber())
                    .firstName("")
                    .lastName("")
                    .profilePhoto(new byte[]{})
                    .dateOfBirth(LocalDate.of(1970, 1, 1))
                    .balance(balance)
                    .loyaltyPoint(loyaltyPoint)
                    .otp(otp)
                    .role(role)
                    .build();

            balance.setUser(user);
            loyaltyPoint.setUser(user);
            pin.setUser(user);
            otp.setUser(user);

            userRepository.saveAndFlush(user);

            log.info("End createNew");
            return mapToResponse(user);
        } catch (DataIntegrityViolationException e) {
            log.error("Error createNew: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone number already exist");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> getAll(SearchUserRequest request) {
        log.info("Start getAll");
        String fieldName = SortingUtil.sortByValidation(User.class, request.getSortBy(), "accountID");
        request.setSortBy(fieldName);

        Sort.Direction direction = Sort.Direction.fromString(request.getDirection());
        Pageable pageable = PageRequest.of(
                request.getPage() - 1,
                request.getSize(),
                direction,
                request.getSortBy()
        );

        Specification<User> specification = getUserSpecification(request);
        Page<User> users = userRepository.findAll(specification, pageable);
        log.info("End getAll");
        return users.map(this::mapToResponse);
    }

    @Override
    public UserResponse getById(String id) {
        log.info("Start getOneById");
        User user = findByIdOrThrowNotFound(id);
        log.info("End getOneById");
        return mapToResponse(user);
    }

    @Override
    public User getUserById(String id) {
        return (findByIdOrThrowNotFound(id));
    }


    @Override
    public UserResponse getUserByPhoneNumber(String phoneNumber) {
        log.info("Start getOneByPhoneNumber");
        Optional<User> byPhoneNumber = userRepository.findByPhoneNumber(phoneNumber);
        log.info("End getOneById");
        return byPhoneNumber.map(this::mapToResponse).orElse(null);
    }

    @Override
    public User getUserByPhoneNumber2(String phoneNumber) {
        log.info("Start getOneByPhoneNumber");
        User user = findByPhoneNumberOrThrowNotFound(phoneNumber);
        log.info("End getOneById");
        return user;
    }

    @Override
    public UserResponse getUserByKtpID(String ktpID) {
        log.info("Start getOneByKtpID");
        Optional<User> user = userRepository.findByKtpID(ktpID);
        log.info("End getOneById");
        return user.map(this::mapToResponse).orElse(null);
    }

    @Override
    public UserResponse update(UpdateUserRequest request) {
        try {
            log.info("Start update");
            validationUtil.validate(request);
            User user = findByIdOrThrowNotFound(request.getAccountID());
            user.setKtpID(request.getKtpID());
            user.setAccountEmail(request.getAccountEmail());
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setDateOfBirth(request.getDateOfBirth());
            userRepository.saveAndFlush(user);
            log.info("End update");
            return mapToResponse(user);
        } catch (Exception e) {
            log.error("Error update: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @Override
    public UserResponse updateProfilePhoto(UpdateUserProfilePhotoRequest request) {
        try {
            log.info("Start update");
            validationUtil.validate(request);
            User user = findByIdOrThrowNotFound(request.getAccountID());

            user.setProfilePhoto(request.getProfilePhoto().getBytes());

            userRepository.saveAndFlush(user);
            log.info("End update");
            return mapToResponse(user);
        } catch (Exception e) {
            log.error("Error update: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


    private UserResponse mapToResponse(User user) {
        List<VoucherResponse> voucherResponses;
        List<Voucher> vouchers;
        if (user.getVouchers() != null) {
            vouchers = user.getVouchers().stream().filter(voucher -> voucher.getVoucherStatus().getStatus() == EVoucherStatus.ACTIVE
            ).collect(Collectors.toList());
            voucherResponses = vouchers.stream().map(this::mapVoucherToResponse).collect(Collectors.toList());
        } else {
            voucherResponses = List.of();
        }
        LoyaltyPointResponse loyaltyPointResponse = loyaltyPointService.findById(user.getLoyaltyPoint().getLoyaltyPointID());
        BalanceResponse balanceResponse = balanceService.findById(user.getBalance().getBalanceID());

        return UserResponse.builder()
                .accountID(user.getAccountID())
                .ktpID(user.getKtpID())
                .accountEmail(user.getAccountEmail())
                .phoneNumber(user.getPhoneNumber())
                .pinID(user.getPin().getPinID())
                .createdAt(user.getCreatedAt())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth().toString())
                .profilePhoto(user.getProfilePhoto())
                .updatedAt(user.getUpdatedAt())
                .balance(balanceResponse)
                .loyaltyPoint(loyaltyPointResponse)
                .otpID(user.getOtp().getOtpID())
                .vouchers(voucherResponses)
                .role(ERole.ROLE_USER.name())
                .build();
    }

    private VoucherResponse mapVoucherToResponse(Voucher voucher) {
        return VoucherResponse.builder()
                .voucherID(voucher.getVoucherID())
                .promotionID(voucher.getPromotion().getPromotionID())
                .merchantID(voucher.getPromotion().getMerchant().getMerchantID())
                .userID(voucher.getUser().getAccountID())
                .voucherValue(voucher.getVoucherValue())
                .expiredDate(voucher.getExpiredDate())
                .voucherCode(voucher.getVoucherCode())
                .voucherStatus(voucher.getVoucherStatus().getStatus().name())
                .createdAt(voucher.getCreatedAt())
                .updatedAt(voucher.getUpdatedAt())
                .promotionDescription(voucher.getPromotion().getPromotionDescription())
                .promotionName(voucher.getPromotion().getPromotionName())
                .logoImage(voucher.getPromotion().getMerchant().getLogoImage())
                .build();
    }

    private User findByIdOrThrowNotFound(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
    }

    private User findByPhoneNumberOrThrowNotFound(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
    }

    private Specification<User> getUserSpecification(SearchUserRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getPhoneNumber() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("phoneNumber")),
                        "%" + request.getPhoneNumber().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }

            if (request.getStartCreatedAt() != null && request.getEndCreatedAt() != null) {
                Timestamp startTimestamp = Timestamp.valueOf(request.getStartCreatedAt().atStartOfDay());
                LocalDateTime endOfTheDay = request.getEndCreatedAt().atTime(LocalTime.MAX);
                Timestamp endTimestamp = Timestamp.valueOf(endOfTheDay);
                Predicate predicate = criteriaBuilder.between(
                        root.get("createdAt"),
                        startTimestamp,
                        endTimestamp
                );
                predicates.add(predicate);
            }

            if (request.getStartUpdatedAt() != null && request.getEndUpdatedAt() != null) {
                Timestamp startTimestamp = Timestamp.valueOf(request.getStartUpdatedAt().atStartOfDay());
                LocalDateTime endOfTheDay = request.getEndUpdatedAt().atTime(LocalTime.MAX);
                Timestamp endTimestamp = Timestamp.valueOf(endOfTheDay);
                Predicate predicate = criteriaBuilder.between(
                        root.get("updatedAt"),
                        startTimestamp,
                        endTimestamp
                );
                predicates.add(predicate);
            }

            return query
                    .where(predicates.toArray(new Predicate[]{}))
                    .getRestriction();
        };
    }
}
