package com.enigma.x_food.feature.voucher;

import com.enigma.x_food.constant.EVoucherStatus;
import com.enigma.x_food.feature.balance.BalanceService;
import com.enigma.x_food.feature.loyalty_point.LoyaltyPointService;
import com.enigma.x_food.feature.otp.OTPService;
import com.enigma.x_food.feature.pin.PinService;
import com.enigma.x_food.feature.promotion.Promotion;
import com.enigma.x_food.feature.promotion.PromotionService;
import com.enigma.x_food.feature.user.User;
import com.enigma.x_food.feature.user.UserService;
import com.enigma.x_food.feature.voucher.dto.response.VoucherResponse;
import com.enigma.x_food.feature.voucher.dto.request.NewVoucherRequest;
import com.enigma.x_food.feature.voucher.dto.request.SearchVoucherRequest;
import com.enigma.x_food.feature.voucher.dto.request.UpdateVoucherRequest;
import com.enigma.x_food.feature.voucher_status.VoucherStatus;
import com.enigma.x_food.feature.voucher_status.VoucherStatusService;
import com.enigma.x_food.security.BCryptUtil;
import com.enigma.x_food.util.SortingUtil;
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
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.Predicate;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoucherServiceImpl implements VoucherService {
    private final VoucherRepository voucherRepository;
    private final VoucherStatusService voucherStatusService ;
    private final PinService pinService;
    private final OTPService otpService;
    private final BalanceService balanceService;
    private final PromotionService promotionService;
    private final UserService userService;
    private final LoyaltyPointService loyaltyPointService;
    private final ValidationUtil validationUtil;
    private final Random random;
    private final BCryptUtil bCryptUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public VoucherResponse createNew(NewVoucherRequest request) {
        try {
            log.info("Start createNew");
            validationUtil.validate(request);

            Promotion promotion = promotionService.getPromotionById(request.getPromotionID());

            User user = userService.getUserById(request.getAccountID());

            VoucherStatus voucherStatus = voucherStatusService.getByStatus(EVoucherStatus.ACTIVE);

            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

            Timestamp expiredAt = new Timestamp(currentTimestamp.toInstant().plusSeconds(3L * 30 * 24 * 60 * 60).toEpochMilli());


            Voucher voucher = Voucher.builder()
                    .promotion(promotion)
                    .user(user)
                    .voucherValue(request.getVoucherValue())
                    .expiredDate(expiredAt)
                    .voucherCode(request.getVoucherCode())
                    .voucherStatus(voucherStatus)
                    .build();

            voucherRepository.saveAndFlush(voucher);

            log.info("End createNew");
            return mapToResponse(voucher);
        } catch (Exception e) {
            log.error("Error createNew: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VoucherResponse> getAll(SearchVoucherRequest request) {
        log.info("Start getAll");
        String fieldName = SortingUtil.sortByValidation(Voucher.class, request.getSortBy(), "accountID");
        request.setSortBy(fieldName);

        Sort.Direction direction = Sort.Direction.fromString(request.getDirection());
        Pageable pageable = PageRequest.of(
                request.getPage() - 1,
                request.getSize(),
                direction,
                request.getSortBy()
        );

        Specification<Voucher> specification = getVoucherSpecification(request);
        Page<Voucher> vouchers = voucherRepository.findAll(specification, pageable);
        log.info("End getAll");
        return vouchers.map(this::mapToResponse);
    }

    @Override
    public VoucherResponse findById(String id) {
        log.info("Start getOneById");
        Voucher voucher = findByIdOrThrowNotFound(id);
        log.info("End getOneById");
        return mapToResponse(voucher);
    }

    @Override
    public Voucher getVoucherById(String id) {
        return (findByIdOrThrowNotFound(id));
    }

    @Override
    public VoucherResponse update(UpdateVoucherRequest request) {
        try {
            log.info("Start update");
            validationUtil.validate(request);
            Voucher voucher = findByIdOrThrowNotFound(request.getVoucherID());
            voucher.setVoucherCode(request.getVoucherCode());
            voucher.setVoucherValue(request.getVoucherValue());
            voucherRepository.saveAndFlush(voucher);
            log.info("End update");
            return mapToResponse(voucher);
        } catch (DataIntegrityViolationException e) {
            log.error("Error update: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @Override
    public void deleteById(String id) {
        log.info("Start deleteById");
        Voucher voucher = findByIdOrThrowNotFound(id);
        voucherRepository.delete(voucher);
        log.info("End deleteById");
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

    private Voucher findByIdOrThrowNotFound(String id) {
        return voucherRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "voucher not found"));
    }

    private Specification<Voucher> getVoucherSpecification(SearchVoucherRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getVoucherID() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("accountID")),
                        "%" + request.getVoucherID().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }

            return query
                    .where(predicates.toArray(new Predicate[]{}))
                    .getRestriction();
        };
    }
}
