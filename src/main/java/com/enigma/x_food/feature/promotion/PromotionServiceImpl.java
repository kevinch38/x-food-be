package com.enigma.x_food.feature.promotion;

import com.enigma.x_food.constant.EActivity;
import com.enigma.x_food.constant.EPromotionStatus;
import com.enigma.x_food.feature.admin.Admin;
import com.enigma.x_food.feature.admin.AdminService;
import com.enigma.x_food.feature.admin_monitoring.AdminMonitoringService;
import com.enigma.x_food.feature.admin_monitoring.dto.request.AdminMonitoringRequest;
import com.enigma.x_food.feature.merchant.Merchant;
import com.enigma.x_food.feature.merchant.MerchantService;
import com.enigma.x_food.feature.promotion.dto.request.NewPromotionRequest;
import com.enigma.x_food.feature.promotion.dto.request.SearchPromotionRequest;
import com.enigma.x_food.feature.promotion.dto.request.UpdatePromotionRequest;
import com.enigma.x_food.feature.promotion.dto.response.PromotionResponse;
import com.enigma.x_food.feature.promotion_status.PromotionStatus;
import com.enigma.x_food.feature.promotion_status.PromotionStatusService;
import com.enigma.x_food.util.SortingUtil;
import com.enigma.x_food.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PromotionServiceImpl implements PromotionService {
    private final PromotionRepository promotionRepository;
    private final MerchantService merchantService;
    private final AdminService adminService;
    private final PromotionStatusService promotionStatusService;
    private final ValidationUtil validationUtil;
    private final AdminMonitoringService adminMonitoringService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PromotionResponse createNew(NewPromotionRequest request) {
        log.info("Start createNew");
        validationUtil.validate(request);
        Merchant merchant = merchantService.getById(request.getMerchantID());
        PromotionStatus promotionStatus = promotionStatusService.getByStatus(EPromotionStatus.ACTIVE);

        Admin admin = adminService.getById("1");
        Promotion promotion = Promotion.builder()
                .merchant(merchant)
                .cost(request.getCost())
                .maxRedeem(request.getMaxRedeem())
                .promotionValue(request.getPromotionValue())
                .promotionDescription(request.getPromotionDescription())
                .promotionName(request.getPromotionName())
                .quantity(request.getQuantity())
                .admin(admin)
                .expiredDate(request.getExpiredDate())
                .promotionStatus(promotionStatus)
                .notes(request.getNotes())
                .build();

        promotionRepository.saveAndFlush(promotion);

        AdminMonitoringRequest adminMonitoringRequest = AdminMonitoringRequest.builder()
                .activity(EActivity.CREATE_PROMOTION.name())
                .adminID("1")
                .build();
        adminMonitoringService.createNew(adminMonitoringRequest);

        log.info("End createNew");
        return mapToResponse(promotion);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PromotionResponse update(UpdatePromotionRequest request) {
        validationUtil.validate(request);
        Promotion promotion = findByIdOrThrowException(request.getPromotionID());

        Merchant merchant = merchantService.getById(request.getMerchantID());
        promotion.setQuantity(request.getQuantity());
        promotion.setMerchant(merchant);
        promotion.setCost(request.getCost());
        promotion.setMaxRedeem(request.getMaxRedeem());
        promotion.setPromotionValue(request.getPromotionValue());
        promotion.setPromotionName(request.getPromotionName());
        promotion.setQuantity(request.getQuantity());
        promotion.setExpiredDate(request.getExpiredDate());
        promotion.setNotes(request.getNotes());

        AdminMonitoringRequest adminMonitoringRequest = AdminMonitoringRequest.builder()
                .activity(EActivity.UPDATE_PROMOTION.name())
                .adminID("1")
                .build();
        adminMonitoringService.createNew(adminMonitoringRequest);

        return mapToResponse(promotionRepository.saveAndFlush(promotion));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String id) {
        validationUtil.validate(id);
        Promotion promotion = findByIdOrThrowException(id);
        PromotionStatus promotionStatus = promotionStatusService.getByStatus(EPromotionStatus.INACTIVE);
        promotion.setPromotionStatus(promotionStatus);

        AdminMonitoringRequest adminMonitoringRequest = AdminMonitoringRequest.builder()
                .activity(EActivity.DELETE_PROMOTION.name())
                .adminID("1")
                .build();
        adminMonitoringService.createNew(adminMonitoringRequest);
        promotionRepository.saveAndFlush(promotion);
    }

    @Scheduled(cron = "0 0 0 * * ?", zone = "GMT+7")
    public void updateExpiredPromotion() {
        List<Promotion> promotions = promotionRepository.findAll();
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        PromotionStatus promotionStatus = promotionStatusService.getByStatus(EPromotionStatus.INACTIVE);

        for (Promotion promotion : promotions) {
            if (promotion.getExpiredDate().before(currentTimestamp)) {
                promotion.setPromotionStatus(promotionStatus);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PromotionResponse findById(String id) {
        validationUtil.validate(id);
        return mapToResponse(findByIdOrThrowException(id));
    }

    @Override
    public Promotion getPromotionById(String id) {
        return findByIdOrThrowException(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PromotionResponse> getAll(SearchPromotionRequest request) {
        String fieldName = SortingUtil.sortByValidation(Promotion.class, request.getSortBy(), "promotionID");
        request.setSortBy(fieldName);

        Sort.Direction direction = Sort.Direction.fromString(request.getDirection());
        Pageable pageable = PageRequest.of(
                request.getPage() - 1,
                request.getSize(),
                direction,
                request.getSortBy()
        );

        Specification<Promotion> specification = getPromotionSpecification(request, "all");
        Page<Promotion> promotions = promotionRepository.findAll(specification, pageable);
        return promotions.map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromotionResponse> getAllActive(SearchPromotionRequest request) {
        String fieldName = SortingUtil.sortByValidation(Promotion.class, request.getSortBy(), "promotionID");
        request.setSortBy(fieldName);

        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());

        Specification<Promotion> specification = getPromotionSpecification(request, "active");
        List<Promotion> promotions = promotionRepository.findAll(specification, sort);
        return promotions.stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private Promotion findByIdOrThrowException(String id) {
        return promotionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Promotion not found"));
    }

    private PromotionResponse mapToResponse(Promotion promotion) {
        return PromotionResponse.builder()
                .promotionID(promotion.getPromotionID())
                .merchantName(promotion.getMerchant().getMerchantName())
                .cost(promotion.getCost())
                .maxRedeem(promotion.getMaxRedeem())
                .promotionValue(promotion.getPromotionValue())
                .promotionDescription(promotion.getPromotionDescription())
                .promotionName(promotion.getPromotionName())
                .quantity(promotion.getQuantity())
                .adminName(promotion.getAdmin().getAdminName())
                .expiredDate(promotion.getExpiredDate())
                .status(promotion.getPromotionStatus().getStatus().name())
                .createdAt(promotion.getCreatedAt())
                .updatedAt(promotion.getUpdatedAt())
                .notes(promotion.getNotes())
                .build();
    }

    private Specification<Promotion> getPromotionSpecification(SearchPromotionRequest request, String option) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getMerchantID() != null) {
                Join<Promotion, Merchant> promotionJoin = root.join("merchant", JoinType.INNER);
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(promotionJoin.get("merchantID")),
                        "%" + request.getMerchantID().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }

            if (request.getPromotionStatus() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("promotionStatus").get("status")),
                        "%" + request.getPromotionStatus().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }

            if (request.getStartCreatedAt() != null && request.getEndCreatedAt() != null) {
                Timestamp startTimestamp = Timestamp.valueOf(request.getStartCreatedAt().atStartOfDay());
                Timestamp endTimestamp = Timestamp.valueOf(request.getEndCreatedAt().atStartOfDay());
                Predicate predicate = criteriaBuilder.between(
                        root.get("createdAt"),
                        startTimestamp,
                        endTimestamp
                );
                predicates.add(predicate);
            }

            if (request.getStartUpdatedAt() != null && request.getEndUpdatedAt() != null) {
                Timestamp startTimestamp = Timestamp.valueOf(request.getStartUpdatedAt().atStartOfDay());
                Timestamp endTimestamp = Timestamp.valueOf(request.getEndUpdatedAt().atStartOfDay());
                Predicate predicate = criteriaBuilder.between(
                        root.get("updatedAt"),
                        startTimestamp,
                        endTimestamp
                );
                predicates.add(predicate);
            }

            if (request.getStartExpiredDate() != null && request.getEndExpiredDate() != null) {
                Timestamp startTimestamp = Timestamp.valueOf(request.getStartExpiredDate().atStartOfDay());
                Timestamp endTimestamp = Timestamp.valueOf(request.getEndExpiredDate().atStartOfDay());
                Predicate predicate = criteriaBuilder.between(
                        root.get("expiredDate"),
                        startTimestamp,
                        endTimestamp
                );
                predicates.add(predicate);
            }

            if (option.equalsIgnoreCase("active")) {
                Predicate predicate = criteriaBuilder.equal(
                        root.get("promotionStatus").get("status"),
                        EPromotionStatus.ACTIVE
                );
                predicates.add(predicate);
            }

            return query
                    .where(predicates.toArray(new Predicate[]{}))
                    .getRestriction();
        };
    }
}
