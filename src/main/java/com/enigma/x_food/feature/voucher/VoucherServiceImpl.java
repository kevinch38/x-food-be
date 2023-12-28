package com.enigma.x_food.feature.voucher;

import com.enigma.x_food.constant.EVoucherStatus;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.Predicate;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoucherServiceImpl implements VoucherService {
    private final VoucherRepository voucherRepository;
    private final VoucherStatusService voucherStatusService ;
    private final PromotionService promotionService;
    private final UserService userService;
    private final ValidationUtil validationUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public VoucherResponse createNew(NewVoucherRequest request) {
        try {
            log.info("Start createNew");
            validationUtil.validate(request);

            Promotion promotion = promotionService.getPromotionById(request.getPromotionID());

            User user = userService.getUserById(request.getAccountID());
            if (user.getLoyaltyPoint().getLoyaltyPointAmount() < promotion.getCost()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Loyalty point is not enough");
            }
            if (findByPromotionId(promotion).size()>= promotion.getMaxRedeem()){
                log.info(String.valueOf(findByPromotionId(promotion).size()));
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You have reached the maximum redeem amount");
            }
            if (promotion.getQuantity()<=0){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Promotion quantity is empty");
            }
            Random r = new Random( System.currentTimeMillis() );
            String random = String.valueOf(10000 + r.nextInt(20000));
            String voucherCode=promotion.getPromotionName()+random;

            VoucherStatus voucherStatus = voucherStatusService.getByStatus(EVoucherStatus.ACTIVE);

            Instant currentTime = Instant.now();
            ZoneId gmtPlus7 = ZoneId.of("GMT+7");
            Instant expiredTime = currentTime.plus(Duration.ofDays(90));
            ZonedDateTime expiredTimeGmtPlus7 = ZonedDateTime.ofInstant(expiredTime, gmtPlus7)
                    .withHour(23)
                    .withMinute(59)
                    .withSecond(59)
                    .withNano(0);

            Timestamp expiredAt = Timestamp.from(expiredTimeGmtPlus7.toInstant());

            Voucher voucher = Voucher.builder()
                    .promotion(promotion)
                    .user(user)
                    .voucherValue(promotion.getPromotionValue())
                    .expiredDate(expiredAt)
                    .voucherCode(voucherCode)
                    .voucherStatus(voucherStatus)
                    .build();

            voucherRepository.saveAndFlush(voucher);

            user.getLoyaltyPoint().setLoyaltyPointAmount(user.getLoyaltyPoint().getLoyaltyPointAmount()-promotion.getCost());
            promotion.setQuantity(promotion.getQuantity()-1);

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

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateExpiredVoucher() {
        List<Voucher> vouchers = voucherRepository.findAll();
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        VoucherStatus voucherStatus = voucherStatusService.getByStatus(EVoucherStatus.INACTIVE);

        for (Voucher voucher : vouchers) {
            if (voucher.getExpiredDate().before(currentTimestamp)) {
                voucher.setVoucherStatus(voucherStatus);
            }
        }
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
    public List<Voucher> getVoucherByPromotion(Promotion promotion) {
        return findByPromotionId(promotion);
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
        VoucherStatus voucherStatus = voucherStatusService.getByStatus(EVoucherStatus.INACTIVE);
        voucher.setVoucherStatus(voucherStatus);
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

    private List<Voucher> findByPromotionId(Promotion promotion) {
        return voucherRepository.findByPromotion(promotion).orElse(List.of());
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
