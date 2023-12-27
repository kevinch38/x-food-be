package com.enigma.x_food.feature.user;

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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
        User user = findByPhoneNumberOrThrowNotFound(phoneNumber);
        log.info("End getOneById");
        return mapToResponse(user);
    }

    @Override
    public User getUserByPhoneNumber2(String phoneNumber) {
        log.info("Start getOneByPhoneNumber");
        User user = findByPhoneNumberOrThrowNotFound(phoneNumber);
        log.info("End getOneById");
        return user;
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
        } catch (DataIntegrityViolationException e) {
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

    @Override
    public void deleteById(String id) {
        log.info("Start deleteById");
        User user = findByIdOrThrowNotFound(id);
        userRepository.delete(user);
        log.info("End deleteById");
    }


    private UserResponse mapToResponse(User user) {
        List<VoucherResponse> vouchers = null;
        if (user.getVouchers() != null) {
            vouchers = user.getVouchers().stream().map(
                    this::mapToResponse
            ).collect(Collectors.toList());
        }
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
                .balanceID(user.getBalance().getBalanceID())
                .loyaltyPointID(user.getLoyaltyPoint().getLoyaltyPointID())
                .otpID(user.getOtp().getOtpID())
                .vouchers(vouchers)
                .build();
    }

    private VoucherResponse mapToResponse(Voucher voucher) {
        return VoucherResponse.builder()
                .voucherID(voucher.getVoucherID())
                .promotionID(voucher.getPromotion().getPromotionID())
                .userID(voucher.getUser().getAccountID())
                .voucherValue(voucher.getVoucherValue())
                .expiredDate(voucher.getExpiredDate())
                .voucherCode(voucher.getVoucherCode())
                .voucherStatus(voucher.getVoucherStatus().getStatus().name())
                .createdAt(voucher.getCreatedAt())
                .updatedAt(voucher.getUpdatedAt())
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

            if (request.getAccountID() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("accountID")),
                        "%" + request.getAccountID().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }

            if (request.getAccountEmail() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("accountEmail")),
                        "%" + request.getAccountEmail().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }

            if (request.getFirstName() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("firstName")),
                        "%" + request.getFirstName().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }

            if (request.getLastName() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("lastName")),
                        "%" + request.getLastName().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }

            if (request.getPhoneNumber() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("phoneNumber")),
                        "%" + request.getPhoneNumber().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }

            return query
                    .where(predicates.toArray(new Predicate[]{}))
                    .getRestriction();
        };
    }
}
