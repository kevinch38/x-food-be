package com.enigma.x_food.feature.merchant;

import com.enigma.x_food.constant.EActivity;
import com.enigma.x_food.constant.EMerchantBranchStatus;
import com.enigma.x_food.constant.EMerchantStatus;
import com.enigma.x_food.feature.admin.Admin;
import com.enigma.x_food.feature.admin_monitoring.AdminMonitoringService;
import com.enigma.x_food.feature.admin_monitoring.dto.request.AdminMonitoringRequest;
import com.enigma.x_food.feature.branch_working_hours.dto.response.BranchWorkingHoursResponse;
import com.enigma.x_food.feature.city.City;
import com.enigma.x_food.feature.city.dto.response.CityResponse;
import com.enigma.x_food.feature.item.Item;
import com.enigma.x_food.feature.item.dto.response.ItemResponse;
import com.enigma.x_food.feature.item_variety.ItemVariety;
import com.enigma.x_food.feature.item_variety.dto.response.ItemVarietyResponse;
import com.enigma.x_food.feature.merchant.dto.request.*;
import com.enigma.x_food.feature.merchant.dto.response.MerchantResponse;
import com.enigma.x_food.feature.merchant_branch.MerchantBranch;
import com.enigma.x_food.feature.merchant_branch.dto.response.MerchantBranchResponse;
import com.enigma.x_food.feature.merchant_branch_status.MerchantBranchStatus;
import com.enigma.x_food.feature.merchant_branch_status.MerchantBranchStatusService;
import com.enigma.x_food.feature.merchant_status.MerchantStatus;
import com.enigma.x_food.feature.merchant_status.MerchantStatusService;
import com.enigma.x_food.feature.sub_variety.dto.response.SubVarietyResponse;
import com.enigma.x_food.feature.variety.dto.response.VarietyResponse;
import com.enigma.x_food.feature.variety_sub_variety.VarietySubVariety;
import com.enigma.x_food.feature.variety_sub_variety.dto.response.VarietySubVarietyResponse;
import com.enigma.x_food.util.SortingUtil;
import com.enigma.x_food.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.Predicate;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {
    private final MerchantRepository merchantRepository;
    private final ValidationUtil validationUtil;
    private final MerchantStatusService merchantStatusService;
    private final AdminMonitoringService adminMonitoringService;
    private final MerchantBranchStatusService merchantBranchStatusService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MerchantResponse createNew(NewMerchantRequest request) throws IOException, AuthenticationException {
        Admin admin;
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            admin = (Admin) authentication.getPrincipal();
        } catch (Exception e) {
            throw new AuthenticationException("Not Authorized");
        }

        validationUtil.validate(request);

        MerchantStatus merchantStatus = merchantStatusService.getByStatus(EMerchantStatus.WAITING_FOR_CREATION_APPROVAL);

        Merchant merchant = Merchant.builder()
                .merchantName(request.getMerchantName())
                .picName(request.getPicName())
                .picNumber(request.getPicNumber())
                .picEmail(request.getPicEmail())
                .merchantDescription(request.getMerchantDescription())
                .admin(admin)
                .merchantStatus(merchantStatus)
                .image(request.getImage().getBytes())
                .logoImage(request.getLogoImage().getBytes())
                .build();
        merchantRepository.saveAndFlush(merchant);


        AdminMonitoringRequest adminMonitoringRequest = AdminMonitoringRequest.builder()
                .activity(EActivity.CREATE_MERCHANT.name())
                .admin(admin)
                .build();
        adminMonitoringService.createNew(adminMonitoringRequest);
        return mapToResponse(merchant);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MerchantResponse> getAllActive(SearchActiveMerchantRequest request) {
        validationUtil.validate(request);
        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());

        Specification<Merchant> specification = getActiveMerchantSpecification(request);

        return merchantRepository.findAll(specification, sort).stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MerchantResponse> getAll(SearchMerchantRequest request) {
        String fieldName = SortingUtil.sortByValidation(Merchant.class, request.getSortBy(), "merchantID");
        request.setSortBy(fieldName);

        Sort.Direction direction = Sort.Direction.fromString(request.getDirection());

        Specification<Merchant> specification = getMerchantSpecification(request);

        Pageable pageable = PageRequest.of(
                request.getPage() - 1,
                request.getSize(),
                direction,
                request.getSortBy()
        );

        Page<Merchant> merchantBranches = merchantRepository.findAll(specification, pageable);
        return merchantBranches.map(this::mapToResponse);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MerchantResponse update(UpdateMerchantRequest request) throws IOException, AuthenticationException {
        Admin admin;
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            admin = (Admin) authentication.getPrincipal();
        } catch (Exception e) {
            throw new AuthenticationException("Not Authorized");
        }

        validationUtil.validate(request);
        Merchant merchant = findByIdOrThrowException(request.getMerchantID());

        MerchantStatus merchantStatus = merchantStatusService.getByStatus(EMerchantStatus.WAITING_FOR_UPDATE_APPROVAL);

        merchant.setMerchantStatus(merchantStatus);
        merchant.setMerchantStatus(merchantStatus);
        merchant.setMerchantName(request.getMerchantName());
        merchant.setPicName(request.getPicName());
        merchant.setPicNumber(request.getPicNumber());
        merchant.setPicEmail(request.getPicEmail());
        merchant.setMerchantDescription(request.getMerchantDescription());
        merchant.setImage(request.getImage().getBytes());
        merchant.setLogoImage(request.getLogoImage().getBytes());

        AdminMonitoringRequest adminMonitoringRequest = AdminMonitoringRequest.builder()
                .activity(EActivity.UPDATE_MERCHANT.name())
                .admin(admin)
                .build();
        adminMonitoringService.createNew(adminMonitoringRequest);
        return mapToResponse(merchantRepository.saveAndFlush(merchant));
    }

    @Override
    @Transactional(readOnly = true)
    public MerchantResponse findById(String id) {
        validationUtil.validate(id);
        return mapToResponse(findByIdOrThrowException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Merchant getById(String id) {
        return findByIdOrThrowException(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String id) throws AuthenticationException {
        Admin admin;
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            admin = (Admin) authentication.getPrincipal();
        } catch (Exception e) {
            throw new AuthenticationException("Not Authorized");
        }

        validationUtil.validate(id);

        Merchant merchant = findByIdOrThrowException(id);
        MerchantStatus merchantStatus = merchantStatusService.getByStatus(EMerchantStatus.WAITING_FOR_DELETION_APPROVAL);

        merchant.setMerchantStatus(merchantStatus);

        for (MerchantBranch merchantBranch : merchant.getMerchantBranches()) {
            MerchantBranchStatus status = merchantBranchStatusService.getByStatus(EMerchantBranchStatus.INACTIVE);
            merchantBranch.setMerchantBranchStatus(status);
        }

        AdminMonitoringRequest adminMonitoringRequest = AdminMonitoringRequest.builder()
                .activity(EActivity.DELETE_MERCHANT.name())
                .admin(admin)
                .build();
        adminMonitoringService.createNew(adminMonitoringRequest);
        merchantRepository.saveAndFlush(merchant);
    }

    @Override
    public void approveToActive(ApprovalMerchantRequest request) {
        updateStatus(request, EMerchantStatus.ACTIVE);
    }

    @Override
    public void deleteApprove(ApprovalMerchantRequest request) {
        updateStatus(request, EMerchantStatus.INACTIVE);
    }

    private void updateStatus(ApprovalMerchantRequest request, EMerchantStatus status) {
        Merchant merchant = findByIdOrThrowException(request.getMerchantID());

        if (status.equals(EMerchantStatus.ACTIVE) &&
                merchant.getMerchantStatus().getStatus().equals(EMerchantStatus.WAITING_FOR_CREATION_APPROVAL))
            merchant.setJoinDate(Timestamp.from(Instant.now()));

        MerchantStatus merchantStatus = merchantStatusService.getByStatus(status);
        merchant.setMerchantStatus(merchantStatus);
        merchant.setNotes(request.getNotes());

        merchantRepository.saveAndFlush(merchant);
    }

    private MerchantResponse mapToResponse(Merchant merchant) {
        List<MerchantBranchResponse> merchantBranches = null;
        if (merchant.getMerchantBranches() != null) {
            merchantBranches = merchant.getMerchantBranches().stream().map(
                    mb -> getMerchantBranchResponse(merchant, mb)
            ).collect(Collectors.toList());
        }

        return MerchantResponse.builder()
                .merchantID(merchant.getMerchantID())
                .joinDate(merchant.getJoinDate())
                .merchantName(merchant.getMerchantName())
                .picName(merchant.getPicName())
                .picNumber(merchant.getPicNumber())
                .picEmail(merchant.getPicEmail())
                .merchantDescription(merchant.getMerchantDescription())
                .adminName(merchant.getAdmin().getAdminName())
                .createdAt(merchant.getCreatedAt())
                .updatedAt(merchant.getUpdatedAt())
                .status(merchant.getMerchantStatus().getStatus().name())
                .notes(merchant.getNotes())
                .merchantBranches(merchantBranches)
                .image(merchant.getImage())
                .logoImage(merchant.getLogoImage())
                .build();
    }

    private static MerchantBranchResponse getMerchantBranchResponse(Merchant merchant, MerchantBranch mb) {
        City city = mb.getCity();

        List<ItemResponse> items = mb.getItems().stream().map(
                MerchantServiceImpl::getItemResponse
        ).collect(Collectors.toList());

        List<BranchWorkingHoursResponse> branchWorkingHoursResponses = mb.getBranchWorkingHours().stream().map(
                bwh -> BranchWorkingHoursResponse.builder()
                        .branchWorkingHoursID(bwh.getBranchWorkingHoursID())
                        .openHour(bwh.getOpenHour())
                        .closeHour(bwh.getCloseHour())
                        .days(bwh.getDays().name())
                        .createdAt(bwh.getCreatedAt())
                        .updatedAt(bwh.getUpdatedAt())
                        .build()
        ).collect(Collectors.toList());

        return MerchantBranchResponse.builder()
                .branchID(mb.getBranchID())
                .merchantID(mb.getMerchant().getMerchantID())
                .branchName(mb.getBranchName())
                .address(mb.getAddress())
                .timezone(mb.getTimezone())
                .createdAt(mb.getCreatedAt())
                .updatedAt(mb.getUpdatedAt())
                .branchWorkingHours(branchWorkingHoursResponses)
                .city(CityResponse.builder()
                        .cityID(city.getCityID())
                        .cityName(city.getCityName())
                        .createdAt(city.getCreatedAt())
                        .updatedAt(city.getUpdatedAt())
                        .build())
                .status(mb.getMerchantBranchStatus().getStatus().name())
                .items(items)
                .picName(merchant.getPicName())
                .picNumber(merchant.getPicNumber())
                .picEmail(merchant.getPicEmail())
                .image(mb.getImage())
                .build();
    }

    private static ItemResponse getItemResponse(Item i) {
        List<ItemVarietyResponse> itemVarietyResponses = i.getItemVarieties().stream().map(
                MerchantServiceImpl::getItemVarietyResponse
        ).collect(Collectors.toList());

        return ItemResponse.builder()
                .itemID(i.getItemID())
                .itemName(i.getItemName())
                .category(i.getCategory())
                .branchID(i.getMerchantBranch().getBranchID())
                .image(i.getImage())
                .initialPrice(i.getInitialPrice())
                .discountedPrice(i.getDiscountedPrice())
                .itemStock(i.getItemStock())
                .isDiscounted(i.getIsDiscounted())
                .isRecommended(i.getIsRecommended())
                .itemDescription(i.getItemDescription())
                .itemVarieties(itemVarietyResponses)
                .build();
    }

    private static ItemVarietyResponse getItemVarietyResponse(ItemVariety iv) {
        VarietyResponse varietyResponse = null;
        if (iv.getVariety() != null) {
            List<VarietySubVarietyResponse> varietySubVarietyResponses = null;
            if (iv.getVariety().getVarietySubVarieties() != null) {
                varietySubVarietyResponses = iv.getVariety().getVarietySubVarieties().stream().map(
                        MerchantServiceImpl::getVarietySubVarietyResponse
                ).collect(Collectors.toList());
            }
            varietyResponse = VarietyResponse.builder()
                    .varietyID(iv.getVariety().getVarietyID())
                    .varietyName(iv.getVariety().getVarietyName())
                    .isRequired(iv.getVariety().getIsRequired())
                    .varietySubVarieties(varietySubVarietyResponses)
                    .build();
        }
        return ItemVarietyResponse.builder()
                .itemVarietyID(iv.getItemVarietyID())
                .variety(varietyResponse)
                .build();
    }

    private static VarietySubVarietyResponse getVarietySubVarietyResponse(VarietySubVariety vsv) {
        SubVarietyResponse subVarietyResponse = null;
        if (vsv.getSubVariety() != null) {
            subVarietyResponse = SubVarietyResponse.builder()
                    .subVarietyID(vsv.getSubVariety().getSubVarietyID())
                    .branchID(vsv.getSubVariety().getMerchantBranch().getBranchID())
                    .subVarName(vsv.getSubVariety().getSubVarName())
                    .subVarStock(vsv.getSubVariety().getSubVarStock())
                    .subVarPrice(vsv.getSubVariety().getSubVarPrice())
                    .build();
        }

        return VarietySubVarietyResponse.builder()
                .varietySubVarietyID(vsv.getVarietySubVarietyID())
                .subVariety(subVarietyResponse)
                .build();
    }

    private Merchant findByIdOrThrowException(String id) {
        return merchantRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Merchant not found")
        );
    }

    private Specification<Merchant> getMerchantSpecification(SearchMerchantRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getMerchantName() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("merchantName")),
                        "%" + request.getMerchantName().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }

            if (request.getMerchantStatus() != null) {
                Predicate predicate = criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("merchantStatus").get("status").as(String.class)),
                        request.getMerchantStatus().toLowerCase()
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

            if (request.getStartJoinDate() != null && request.getEndJoinDate() != null) {
                Timestamp startTimestamp = Timestamp.valueOf(request.getStartJoinDate().atStartOfDay());
                Timestamp endTimestamp = Timestamp.valueOf(request.getEndJoinDate().atStartOfDay());
                Predicate predicate = criteriaBuilder.between(
                        root.get("joinDate"),
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

    private Specification<Merchant> getActiveMerchantSpecification(SearchActiveMerchantRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getMerchantName() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("merchantName")),
                        "%" + request.getMerchantName().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }

            Predicate predicate = criteriaBuilder.equal(
                    root.get("merchantStatus").get("status"),
                    EMerchantStatus.ACTIVE
            );
            predicates.add(predicate);

            return query
                    .where(predicates.toArray(new Predicate[]{}))
                    .getRestriction();
        };
    }
}
