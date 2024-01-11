package com.enigma.x_food.feature.merchant_branch;

import com.enigma.x_food.constant.EActivity;
import com.enigma.x_food.constant.EMerchantBranchStatus;
import com.enigma.x_food.feature.admin.Admin;
import com.enigma.x_food.feature.admin_monitoring.AdminMonitoringService;
import com.enigma.x_food.feature.admin_monitoring.dto.request.AdminMonitoringRequest;
import com.enigma.x_food.feature.branch_working_hours.BranchWorkingHours;
import com.enigma.x_food.feature.branch_working_hours.BranchWorkingHoursService;
import com.enigma.x_food.feature.branch_working_hours.dto.response.BranchWorkingHoursResponse;
import com.enigma.x_food.feature.city.City;
import com.enigma.x_food.feature.city.CityService;
import com.enigma.x_food.feature.city.dto.response.CityResponse;
import com.enigma.x_food.feature.item.Item;
import com.enigma.x_food.feature.item.dto.response.ItemResponse;
import com.enigma.x_food.feature.item_variety.ItemVariety;
import com.enigma.x_food.feature.item_variety.dto.response.ItemVarietyResponse;
import com.enigma.x_food.feature.merchant.Merchant;
import com.enigma.x_food.feature.merchant.MerchantService;
import com.enigma.x_food.feature.merchant_branch.dto.request.*;
import com.enigma.x_food.feature.merchant_branch.dto.response.MerchantBranchResponse;
import com.enigma.x_food.feature.merchant_branch_status.MerchantBranchStatus;
import com.enigma.x_food.feature.merchant_branch_status.MerchantBranchStatusService;
import com.enigma.x_food.feature.merchant_branch_update_request.MerchantBranchUpdateRequestService;
import com.enigma.x_food.feature.sub_variety.dto.response.SubVarietyResponse;
import com.enigma.x_food.feature.variety.dto.response.VarietyResponse;
import com.enigma.x_food.feature.variety_sub_variety.VarietySubVariety;
import com.enigma.x_food.feature.variety_sub_variety.dto.response.VarietySubVarietyResponse;
import com.enigma.x_food.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MerchantBranchServiceImpl implements MerchantBranchService {
    private final MerchantBranchRepository merchantBranchRepository;
    private final MerchantService merchantService;
    private final MerchantBranchStatusService merchantBranchStatusService;
    private final BranchWorkingHoursService branchWorkingHoursService;
    private final MerchantBranchUpdateRequestService merchantBranchUpdateRequestService;
    private final ValidationUtil validationUtil;
    private final CityService cityService;
    private final AdminMonitoringService adminMonitoringService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MerchantBranchResponse createNew(NewMerchantBranchRequest request) throws IOException, AuthenticationException {
        Admin admin;
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            admin = (Admin) authentication.getPrincipal();
        } catch (Exception e) {
            throw new AuthenticationException("Not Authorized");
        }

        validationUtil.validate(request);
        Merchant merchant = merchantService.getById(request.getMerchantID());

        CityResponse cityResponse = cityService.getById(request.getCityID());

        MerchantBranchStatus merchantBranchStatus = merchantBranchStatusService.getByStatus(EMerchantBranchStatus.WAITING_FOR_CREATION_APPROVAL);

        List<BranchWorkingHours> branchWorkingHours = branchWorkingHoursService.createNew(request.getBranchWorkingHours());

        MerchantBranch branch = MerchantBranch.builder()
                .merchant(merchant)
                .branchName(request.getBranchName())
                .address(request.getAddress())
                .timezone(request.getTimezone())
                .branchWorkingHours(branchWorkingHours)
                .picName(request.getPicName())
                .picNumber(request.getPicNumber())
                .picEmail(request.getPicEmail())
                .city(City.builder()
                        .cityID(cityResponse.getCityID())
                        .cityName(cityResponse.getCityName())
                        .build())
                .merchantBranchStatus(merchantBranchStatus)
                .admin(admin)
                .build();

        merchantBranchRepository.saveAndFlush(branch);

        for (BranchWorkingHours branchWorkingHour : branchWorkingHours)
            branchWorkingHour.setMerchantBranch(branch);

        AdminMonitoringRequest adminMonitoringRequest = AdminMonitoringRequest.builder()
                .activity(EActivity.CREATE_BRANCH.name())
                .admin(admin)
                .build();
        adminMonitoringService.createNew(adminMonitoringRequest);
        return mapToResponse(branch);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MerchantBranchResponse update(UpdateMerchantBranchRequest request) throws IOException, AuthenticationException {
        validationUtil.validate(request);
        Admin admin;
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            admin = (Admin) authentication.getPrincipal();
        } catch (Exception e) {
            throw new AuthenticationException("Not Authorized");
        }

        MerchantBranch merchantBranch = findByIdOrThrowException(request.getBranchID());
        merchantBranchUpdateRequestService.save(merchantBranch);

        CityResponse cityResponse = cityService.getById(request.getCityID());
        MerchantBranchStatus merchantBranchStatus = merchantBranchStatusService.getByStatus(EMerchantBranchStatus.WAITING_FOR_UPDATE_APPROVAL);

        merchantBranch.setMerchantBranchStatus(merchantBranchStatus);
        merchantBranch.setBranchName(request.getBranchName());
        merchantBranch.setAddress(request.getAddress());
        merchantBranch.setTimezone(request.getTimezone());
        merchantBranch.setPicName(request.getPicName());
        merchantBranch.setPicNumber(request.getPicNumber());
        merchantBranch.setPicEmail(request.getPicEmail());
        merchantBranch.setCity(City.builder()
                .cityID(cityResponse.getCityID())
                .cityName(cityResponse.getCityName())
                .build());
        
        branchWorkingHoursService.update(request.getBranchWorkingHours());
        merchantBranchRepository.saveAndFlush(merchantBranch);

        AdminMonitoringRequest adminMonitoringRequest = AdminMonitoringRequest.builder()
                .activity(EActivity.UPDATE_BRANCH.name())
                .admin(admin)
                .build();

        adminMonitoringService.createNew(adminMonitoringRequest);

        return mapToResponse(merchantBranch);
    }

    @Override
    @Transactional(readOnly = true)
    public MerchantBranchResponse findById(String id) {
        validationUtil.validate(id);
        return mapToResponse(findByIdOrThrowException(id));
    }

    @Override
    public MerchantBranch getById(String id) {
        return findByIdOrThrowException(id);
    }

    @Override
    public List<MerchantBranchResponse> findAllByMerchantId(SearchMerchantBranchRequest request) {
        validationUtil.validate(request);
        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());

        Specification<MerchantBranch> specification = getMerchantBranchSpecification(request);
        List<MerchantBranch> branches = merchantBranchRepository.findAll(specification, sort);
        return branches.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MerchantBranchResponse> findAllActiveByMerchantId(SearchActiveMerchantBranchRequest request) {
        validationUtil.validate(request);
        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());

        Specification<MerchantBranch> specification = getActiveMerchantBranchSpecification(request);
        List<MerchantBranch> branches = merchantBranchRepository.findAll(specification, sort);
        return branches.stream().map(this::mapToResponse).collect(Collectors.toList());
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

        MerchantBranch merchantBranch = findByIdOrThrowException(id);
        MerchantBranchStatus merchantBranchStatus = merchantBranchStatusService.getByStatus(EMerchantBranchStatus.WAITING_FOR_DELETION_APPROVAL);

        merchantBranch.setMerchantBranchStatus(merchantBranchStatus);

        AdminMonitoringRequest adminMonitoringRequest = AdminMonitoringRequest.builder()
                .activity(EActivity.DELETE_BRANCH.name())
                .admin(admin)
                .build();

        adminMonitoringService.createNew(adminMonitoringRequest);
        merchantBranchRepository.saveAndFlush(merchantBranch);
    }

    @Override
    public void deleteApprove(String id) throws AuthenticationException {
        updateStatus(id, EMerchantBranchStatus.INACTIVE);
    }

    @Override
    public void approveToActive(String id) throws AuthenticationException {
        updateStatus(id, EMerchantBranchStatus.ACTIVE);
    }

    @Override
    public void rejectUpdate(String id) {
        MerchantBranch merchantBranch = merchantBranchUpdateRequestService.getById(id);

        merchantBranchRepository.saveAndFlush(merchantBranch);
    }

    @Override
    public MerchantBranchResponse updateImage(UpdateImageMerchantBranchRequest request) throws IOException {
        MerchantBranch merchantBranch = findByIdOrThrowException(request.getMerchantBranchID());

        merchantBranch.setImage(request.getImage().getBytes());

        return mapToResponse(merchantBranchRepository.saveAndFlush(merchantBranch));
    }

    private void updateStatus(String id, EMerchantBranchStatus status) throws AuthenticationException {
        MerchantBranch merchantBranch = findByIdOrThrowException(id);
        Admin admin;
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            admin = (Admin) authentication.getPrincipal();
        } catch (Exception e) {
            throw new AuthenticationException("Not Authorized");
        }

        if (status.equals(EMerchantBranchStatus.ACTIVE) &&
                merchantBranch.getMerchantBranchStatus().getStatus().equals(EMerchantBranchStatus.WAITING_FOR_CREATION_APPROVAL)) {
            merchantBranch.setJoinDate(Timestamp.from(Instant.now()));
            saveAdminMonitoring(EActivity.APPROVE_CREATE_BRANCH, admin);
        }
        else if (status.equals(EMerchantBranchStatus.INACTIVE) &&
                merchantBranch.getMerchantBranchStatus().getStatus().equals(EMerchantBranchStatus.WAITING_FOR_CREATION_APPROVAL)) {
            saveAdminMonitoring(EActivity.REJECT_CREATE_BRANCH, admin);
        }
        else if (status.equals(EMerchantBranchStatus.ACTIVE) &&
                merchantBranch.getMerchantBranchStatus().getStatus().equals(EMerchantBranchStatus.WAITING_FOR_DELETION_APPROVAL)) {
            saveAdminMonitoring(EActivity.REJECT_DELETE_BRANCH, admin);
        }
        else if (status.equals(EMerchantBranchStatus.INACTIVE) &&
                merchantBranch.getMerchantBranchStatus().getStatus().equals(EMerchantBranchStatus.WAITING_FOR_DELETION_APPROVAL)) {
            saveAdminMonitoring(EActivity.APPROVE_DELETE_BRANCH, admin);
        }
        else if (status.equals(EMerchantBranchStatus.ACTIVE) &&
                merchantBranch.getMerchantBranchStatus().getStatus().equals(EMerchantBranchStatus.WAITING_FOR_UPDATE_APPROVAL)) {
            saveAdminMonitoring(EActivity.APPROVE_UPDATE_BRANCH, admin);
        }
        else if (status.equals(EMerchantBranchStatus.INACTIVE) &&
                merchantBranch.getMerchantBranchStatus().getStatus().equals(EMerchantBranchStatus.WAITING_FOR_UPDATE_APPROVAL)) {
            saveAdminMonitoring(EActivity.REJECT_UPDATE_BRANCH, admin);
        }

        MerchantBranchStatus merchantBranchStatus = merchantBranchStatusService.getByStatus(status);
        merchantBranch.setMerchantBranchStatus(merchantBranchStatus);

        merchantBranchRepository.saveAndFlush(merchantBranch);
    }

    private MerchantBranch findByIdOrThrowException(String id) {
        return merchantBranchRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Merchant branch not found"));
    }

    private void saveAdminMonitoring(EActivity deleteMerchant, Admin admin) {
        AdminMonitoringRequest adminMonitoringRequest = AdminMonitoringRequest.builder()
                .activity(deleteMerchant.name())
                .admin(admin)
                .build();
        adminMonitoringService.createNew(adminMonitoringRequest);
    }

    private MerchantBranchResponse mapToResponse(MerchantBranch branch) {
        City city = branch.getCity();
        List<ItemResponse> items = null;

        if (branch.getItems() != null) {
            items = branch.getItems().stream().map(
                    MerchantBranchServiceImpl::getItemResponse
            ).collect(Collectors.toList());
        }

        List<BranchWorkingHoursResponse> branchWorkingHoursResponses = branch.getBranchWorkingHours().stream().map(
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
                .branchID(branch.getBranchID())
                .merchantID(branch.getMerchant().getMerchantID())
                .branchName(branch.getBranchName())
                .address(branch.getAddress())
                .timezone(branch.getTimezone())
                .createdAt(branch.getCreatedAt())
                .updatedAt(branch.getUpdatedAt())
                .branchWorkingHours(branchWorkingHoursResponses)
                .city(
                        CityResponse.builder()
                                .cityID(city.getCityID())
                                .cityName(city.getCityName())
                                .createdAt(city.getCreatedAt())
                                .updatedAt(city.getUpdatedAt())
                                .build()
                )
                .status(branch.getMerchantBranchStatus().getStatus().name())
                .items(items)
                .picName(branch.getPicName())
                .picNumber(branch.getPicNumber())
                .picEmail(branch.getPicEmail())
                .image(branch.getImage())
                .joinDate(branch.getJoinDate())
                .adminID(branch.getAdmin().getAdminID())
                .build();
    }

    private static ItemResponse getItemResponse(Item i) {
        List<ItemVarietyResponse> itemVarietyResponses = null;
        if (i.getItemVarieties() != null) {
            itemVarietyResponses = i.getItemVarieties().stream().map(
                    MerchantBranchServiceImpl::getItemVarietyResponse
            ).collect(Collectors.toList());
        }

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
                        MerchantBranchServiceImpl::getVarietySubVarietyResponse
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

    private Specification<MerchantBranch> getMerchantBranchSpecification(SearchMerchantBranchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getMerchantID() != null) {
                Join<MerchantBranch, Merchant> merchantJoin = root.join("merchant", JoinType.INNER);
                Predicate predicate = criteriaBuilder.equal(
                        criteriaBuilder.lower(merchantJoin.get("merchantID")),
                        request.getMerchantID().toLowerCase()
                );
                predicates.add(predicate);
            }

            if (request.getBranchID() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("branchID")),
                        "%" + request.getBranchID().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }

            if (request.getBranchName() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("branchName")),
                        "%" + request.getBranchName().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }

            if (request.getCity() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("city").get("cityName")),
                        "%" + request.getCity().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }

            if (request.getStatus() != null) {
                Predicate predicate = criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("merchantBranchStatus").get("status").as(String.class)),
                        request.getStatus().toLowerCase()
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

    private Specification<MerchantBranch> getActiveMerchantBranchSpecification(SearchActiveMerchantBranchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getMerchantID() != null) {
                Join<MerchantBranch, Merchant> merchantJoin = root.join("merchant", JoinType.INNER);
                Predicate predicate = criteriaBuilder.equal(
                        criteriaBuilder.lower(merchantJoin.get("merchantID")),
                        request.getMerchantID().toLowerCase()
                );
                predicates.add(predicate);
            }

            if (request.getBranchID() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("branchID")),
                        "%" + request.getBranchID().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }

            if (request.getBranchName() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("branchName")),
                        "%" + request.getBranchName().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }

            if (request.getCity() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("city").get("cityName")),
                        "%" + request.getCity().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }

            if (request.getStatus() != null) {
                Predicate predicate = criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("merchantBranchStatus").get("status").as(String.class)),
                        request.getStatus().toLowerCase()
                );
                predicates.add(predicate);
            }


            Predicate predicate = criteriaBuilder.equal(
                    root.get("merchantBranchStatus").get("status"),
                    EMerchantBranchStatus.ACTIVE
            );
            predicates.add(predicate);

            return query
                    .where(predicates.toArray(new Predicate[]{}))
                    .getRestriction();
        };
    }
}
