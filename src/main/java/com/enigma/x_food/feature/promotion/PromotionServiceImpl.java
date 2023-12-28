package com.enigma.x_food.feature.promotion;

import com.enigma.x_food.constant.EPromotionStatus;
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
    private final PromotionStatusService promotionStatusService;
    private final ValidationUtil validationUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PromotionResponse createNew(NewPromotionRequest request) {
        log.info("Start createNew");
        validationUtil.validate(request);
        Merchant merchant = merchantService.getById(request.getMerchantID());
        PromotionStatus promotionStatus = promotionStatusService.getByStatus(EPromotionStatus.ACTIVE);

        Promotion promotion = Promotion.builder()
                .merchant(merchant)
                .cost(request.getCost())
                .maxRedeem(request.getMaxRedeem())
                .promotionValue(request.getPromotionValue())
                .promotionDescription(request.getPromotionDescription())
                .promotionName(request.getPromotionName())
                .quantity(request.getQuantity())
                .adminID("adminID")
                .expiredDate(request.getExpiredDate())
                .promotionStatus(promotionStatus)
                .notes(request.getNotes())
                .build();

        promotionRepository.saveAndFlush(promotion);
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

        return mapToResponse(promotionRepository.saveAndFlush(promotion));
    }

    @Scheduled(cron = "0 0 0 1 * ?")
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String id) {
        validationUtil.validate(id);
        Promotion promotion = findByIdOrThrowException(id);
        PromotionStatus promotionStatus = promotionStatusService.getByStatus(EPromotionStatus.INACTIVE);
        promotion.setPromotionStatus(promotionStatus);

        promotionRepository.saveAndFlush(promotion);
    }

    private Promotion findByIdOrThrowException(String id) {
        return promotionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Promotion not found"));
    }

    private PromotionResponse mapToResponse(Promotion promotion) {
        return PromotionResponse.builder()
                .promotionID(promotion.getPromotionID())
                .merchantID(promotion.getMerchant().getMerchantID())
                .cost(promotion.getCost())
                .maxRedeem(promotion.getMaxRedeem())
                .promotionValue(promotion.getPromotionValue())
                .promotionDescription(promotion.getPromotionDescription())
                .promotionName(promotion.getPromotionName())
                .quantity(promotion.getQuantity())
                .adminID(promotion.getAdminID())
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

            if (request.getPromotionID() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("promotionID")),
                        "%" + request.getPromotionID().toLowerCase() + "%"
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

            if (request.getPromotionDescription() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("promotionDescription")),
                        "%" + request.getPromotionDescription().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }

            if (request.getPromotionName() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("promotionName")),
                        "%" + request.getPromotionName().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }

            if (request.getAdminID() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("adminID")),
                        "%" + request.getAdminID().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }

            if (request.getPromotionStatusID() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("promotionStatusID")),
                        "%" + request.getPromotionStatusID().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }
            if (request.getNote() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("notes")),
                        "%" + request.getNote().toLowerCase() + "%"
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
