package com.enigma.x_food.feature.promotion;

import com.enigma.x_food.constant.EActivity;
import com.enigma.x_food.constant.EPromotionStatus;
import com.enigma.x_food.feature.admin.Admin;
import com.enigma.x_food.feature.admin_monitoring.AdminMonitoringService;
import com.enigma.x_food.feature.admin_monitoring.dto.request.AdminMonitoringRequest;
import com.enigma.x_food.feature.merchant.Merchant;
import com.enigma.x_food.feature.merchant.MerchantService;
import com.enigma.x_food.feature.promotion.dto.request.*;
import com.enigma.x_food.feature.promotion.dto.response.PromotionResponse;
import com.enigma.x_food.feature.promotion_status.PromotionStatus;
import com.enigma.x_food.feature.promotion_status.PromotionStatusService;
import com.enigma.x_food.feature.promotion_update_request.PromotionUpdateRequestService;
import com.enigma.x_food.util.SortingUtil;
import com.enigma.x_food.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PromotionServiceImpl implements PromotionService {
    private final PromotionRepository promotionRepository;
    private final MerchantService merchantService;
    private final PromotionStatusService promotionStatusService;
    private final ValidationUtil validationUtil;
    private final AdminMonitoringService adminMonitoringService;
    private final PromotionUpdateRequestService promotionUpdateRequestService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PromotionResponse createNew(NewPromotionRequest request) throws AuthenticationException {
        log.info("Start createNew");
        Admin admin;
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            admin = (Admin) authentication.getPrincipal();
        } catch (Exception e) {
            throw new AuthenticationException("Not Authorized");
        }

        validationUtil.validate(request);
        Merchant merchant = merchantService.getById(request.getMerchantID());
        PromotionStatus promotionStatus = promotionStatusService.getByStatus(EPromotionStatus.WAITING_FOR_CREATION_APPROVAL);

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
                .build();

        promotionRepository.saveAndFlush(promotion);

        saveAdminMonitoring(EActivity.CREATE_PROMOTION, admin);

        log.info("End createNew");
        return mapToResponse(promotion);
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

        Specification<Promotion> specification = getPromotionSpecification(request);
        Page<Promotion> promotions = promotionRepository.findAll(specification, pageable);
        return promotions.map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromotionResponse> getAllActive(SearchActivePromotionRequest request) {
        String fieldName = SortingUtil.sortByValidation(Promotion.class, request.getSortBy(), "promotionID");
        request.setSortBy(fieldName);

        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());

        Specification<Promotion> specification = getActivePromotionSpecification(request);
        List<Promotion> promotions = promotionRepository.findAll(specification, sort);
        return promotions.stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PromotionResponse update(UpdatePromotionRequest request) throws AuthenticationException {
        validationUtil.validate(request);
        Promotion promotion = findByIdOrThrowException(request.getPromotionID());

        promotionUpdateRequestService.save(promotion);

        PromotionStatus promotionStatus = promotionStatusService.getByStatus(EPromotionStatus.WAITING_FOR_CREATION_APPROVAL);

        Admin admin;
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            admin = (Admin) authentication.getPrincipal();
        } catch (Exception e) {
            throw new AuthenticationException("Not Authorized");
        }

        Merchant merchant = merchantService.getById(request.getMerchantID());
        promotion.setPromotionStatus(promotionStatus);
        promotion.setQuantity(request.getQuantity());
        promotion.setMerchant(merchant);
        promotion.setCost(request.getCost());
        promotion.setMaxRedeem(request.getMaxRedeem());
        promotion.setPromotionValue(request.getPromotionValue());
        promotion.setPromotionName(request.getPromotionName());
        promotion.setQuantity(request.getQuantity());
        promotion.setExpiredDate(request.getExpiredDate());

        saveAdminMonitoring(EActivity.UPDATE_PROMOTION, admin);

        return mapToResponse(promotionRepository.saveAndFlush(promotion));
    }

    @Override
    public void deleteApprove(ApprovalPromotionRequest request) throws AuthenticationException {
        updateStatus(request, EPromotionStatus.INACTIVE);
    }

    @Override
    public void rejectUpdate(ApprovalPromotionRequest request) {
        Promotion promotion = promotionUpdateRequestService.getById(request.getPromotionID());

        promotion.setNotes(request.getNotes());
        promotionRepository.saveAndFlush(promotion);
    }

    @Override
    public void approveToActive(ApprovalPromotionRequest request) throws AuthenticationException {
        updateStatus(request, EPromotionStatus.ACTIVE);
    }

    private void updateStatus(ApprovalPromotionRequest request, EPromotionStatus status) throws AuthenticationException {
        Promotion promotion = findByIdOrThrowException(request.getPromotionID());
        Admin admin;
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            admin = (Admin) authentication.getPrincipal();
        } catch (Exception e) {
            throw new AuthenticationException("Not Authorized");
        }

        if (status.equals(EPromotionStatus.ACTIVE) &&
                promotion.getPromotionStatus().getStatus().equals(EPromotionStatus.WAITING_FOR_CREATION_APPROVAL)) {
            saveAdminMonitoring(EActivity.APPROVE_CREATE_PROMOTION, admin);
        }
        else if (status.equals(EPromotionStatus.INACTIVE) &&
                promotion.getPromotionStatus().getStatus().equals(EPromotionStatus.WAITING_FOR_CREATION_APPROVAL)) {
            saveAdminMonitoring(EActivity.REJECT_CREATE_PROMOTION, admin);
        }
        else if (status.equals(EPromotionStatus.ACTIVE) &&
                promotion.getPromotionStatus().getStatus().equals(EPromotionStatus.WAITING_FOR_DELETION_APPROVAL)) {
            saveAdminMonitoring(EActivity.REJECT_DELETE_PROMOTION, admin);
        }
        else if (status.equals(EPromotionStatus.INACTIVE) &&
                promotion.getPromotionStatus().getStatus().equals(EPromotionStatus.WAITING_FOR_DELETION_APPROVAL)) {
            saveAdminMonitoring(EActivity.APPROVE_DELETE_PROMOTION, admin);
        }
        else if (status.equals(EPromotionStatus.ACTIVE) &&
                promotion.getPromotionStatus().getStatus().equals(EPromotionStatus.WAITING_FOR_UPDATE_APPROVAL)) {
            saveAdminMonitoring(EActivity.APPROVE_UPDATE_PROMOTION, admin);
        }
        else if (status.equals(EPromotionStatus.INACTIVE) &&
                promotion.getPromotionStatus().getStatus().equals(EPromotionStatus.WAITING_FOR_UPDATE_APPROVAL)) {
            saveAdminMonitoring(EActivity.REJECT_UPDATE_PROMOTION, admin);
        }

        PromotionStatus promotionStatus = promotionStatusService.getByStatus(status);
        promotion.setPromotionStatus(promotionStatus);
        promotion.setNotes(request.getNotes());

        promotionRepository.saveAndFlush(promotion);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String id) throws AuthenticationException {
        validationUtil.validate(id);
        Promotion promotion = findByIdOrThrowException(id);
        PromotionStatus promotionStatus = promotionStatusService.getByStatus(EPromotionStatus.WAITING_FOR_DELETION_APPROVAL);

        Admin admin;
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            admin = (Admin) authentication.getPrincipal();
        } catch (Exception e) {
            throw new AuthenticationException("Not Authorized");
        }

        promotion.setPromotionStatus(promotionStatus);

        saveAdminMonitoring(EActivity.DELETE_PROMOTION, admin);
        promotionRepository.saveAndFlush(promotion);
    }

    private void saveAdminMonitoring(EActivity updatePromotion, Admin admin) {
        AdminMonitoringRequest adminMonitoringRequest = AdminMonitoringRequest.builder()
                .activity(updatePromotion.name())
                .admin(admin)
                .build();
        adminMonitoringService.createNew(adminMonitoringRequest);
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
                .merchantImage(promotion.getMerchant().getImage())
                .quantity(promotion.getQuantity())
                .adminName(promotion.getAdmin().getAdminName())
                .expiredDate(promotion.getExpiredDate())
                .status(promotion.getPromotionStatus().getStatus().name())
                .createdAt(promotion.getCreatedAt())
                .updatedAt(promotion.getUpdatedAt())
                .notes(promotion.getNotes())
                .build();
    }

    private Specification<Promotion> getPromotionSpecification(SearchPromotionRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getPromotionName() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("promotionName")),
                        "%" + request.getPromotionName().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }

            if (request.getMerchantID() != null) {
                Join<Promotion, Merchant> promotionJoin = root.join("merchant", JoinType.INNER);
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(promotionJoin.get("merchantID")),
                        "%" + request.getMerchantID().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }

            if (request.getPromotionStatus() != null) {
                Predicate predicate = criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("promotionStatus").get("status").as(String.class)),
                        request.getPromotionStatus().toLowerCase()
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

            if (request.getStartExpiredDate() != null && request.getEndExpiredDate() != null) {
                Timestamp startTimestamp = Timestamp.valueOf(request.getStartExpiredDate().atStartOfDay());
                LocalDateTime endOfTheDay = request.getEndExpiredDate().atTime(LocalTime.MAX);
                Timestamp endTimestamp = Timestamp.valueOf(endOfTheDay);
                Predicate predicate = criteriaBuilder.between(
                        root.get("expiredDate"),
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

    private Specification<Promotion> getActivePromotionSpecification(SearchActivePromotionRequest request) {
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

            Predicate predicate = criteriaBuilder.equal(
                    root.get("promotionStatus").get("status"),
                    EPromotionStatus.ACTIVE
            );
            predicates.add(predicate);

            return query
                    .where(predicates.toArray(new Predicate[]{}))
                    .getRestriction();
        };
    }
}
