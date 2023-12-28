package com.enigma.x_food.feature.merchant;

import com.enigma.x_food.constant.EMerchantBranchStatus;
import com.enigma.x_food.constant.EMerchantStatus;
import com.enigma.x_food.feature.city.City;
import com.enigma.x_food.feature.city.dto.response.CityResponse;
import com.enigma.x_food.feature.item.Item;
import com.enigma.x_food.feature.item.dto.response.ItemResponse;
import com.enigma.x_food.feature.item_variety.ItemVariety;
import com.enigma.x_food.feature.item_variety.dto.response.ItemVarietyResponse;
import com.enigma.x_food.feature.merchant.dto.request.NewMerchantRequest;
import com.enigma.x_food.feature.merchant.dto.request.SearchMerchantRequest;
import com.enigma.x_food.feature.merchant.dto.request.UpdateMerchantRequest;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Predicate;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {
    private final MerchantRepository merchantRepository;
    private final ValidationUtil validationUtil;
    private final EntityManager entityManager;
    private final MerchantStatusService merchantStatusService;
    private final MerchantBranchStatusService merchantBranchStatusService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MerchantResponse createNew(NewMerchantRequest request) throws IOException {
        validationUtil.validate(request);

        MerchantStatus merchantStatus = merchantStatusService.getByStatus(EMerchantStatus.ACTIVE);
        Merchant merchant = Merchant.builder()
                .joinDate(request.getJoinDate())
                .merchantName(request.getMerchantName())
                .picName(request.getPicName())
                .picNumber(request.getPicNumber())
                .picEmail(request.getPicEmail())
                .merchantDescription(request.getMerchantDescription())
                .adminID("")
                .merchantStatus(entityManager.merge(merchantStatus))
                .notes(request.getNotes())
                .image(request.getImage().getBytes())
                .logoImage(request.getLogoImage().getBytes())
                .build();
        merchantRepository.saveAndFlush(merchant);
        return mapToResponse(merchant);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MerchantResponse update(UpdateMerchantRequest request) throws IOException {
        validationUtil.validate(request);
        Merchant merchant = findByIdOrThrowException(request.getMerchantID());

        merchant.setMerchantName(request.getMerchantName());
        merchant.setPicName(request.getPicName());
        merchant.setPicNumber(request.getPicNumber());
        merchant.setPicEmail(request.getPicEmail());
        merchant.setMerchantDescription(request.getMerchantDescription());
        merchant.setNotes(request.getNotes());
        merchant.setImage(request.getImage().getBytes());
        merchant.setLogoImage(request.getLogoImage().getBytes());

        return mapToResponse(merchantRepository.saveAndFlush(merchant));
    }

    @Override
    @Transactional(readOnly = true)
    public MerchantResponse findById(String id) {
        validationUtil.validate(id);
        return mapToResponse(findByIdOrThrowException(id));
    }

    @Override
    public Merchant getById(String id) {
        return findByIdOrThrowException(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String id) {
        validationUtil.validate(id);
        Merchant merchant = findByIdOrThrowException(id);
        MerchantStatus merchantStatus = merchantStatusService.getByStatus(EMerchantStatus.INACTIVE);
        merchant.setMerchantStatus(merchantStatus);

        for (MerchantBranch merchantBranch : merchant.getMerchantBranches()) {
            MerchantBranchStatus status = merchantBranchStatusService.getByStatus(EMerchantBranchStatus.INACTIVE);
            merchantBranch.setMerchantBranchStatus(status);
        }

        merchantRepository.saveAndFlush(merchant);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MerchantResponse> getAllActive(SearchMerchantRequest request) {
        validationUtil.validate(request);
        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());

        Specification<Merchant> specification = getMerchantSpecification(request, "active");

        return merchantRepository.findAll(specification, sort).stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MerchantResponse> getAll(SearchMerchantRequest request) {
        String fieldName = SortingUtil.sortByValidation(Merchant.class, request.getSortBy(), "merchantID");
        request.setSortBy(fieldName);

        Sort.Direction direction = Sort.Direction.fromString(request.getDirection());

        Specification<Merchant> specification = getMerchantSpecification(request, "all");

        Pageable pageable = PageRequest.of(
                request.getPage() - 1,
                request.getSize(),
                direction,
                request.getSortBy()
        );

        Page<Merchant> merchantBranches = merchantRepository.findAll(specification, pageable);
        return merchantBranches.map(this::mapToResponse);
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
                .adminId(merchant.getAdminID())
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

        return MerchantBranchResponse.builder()
                .branchID(mb.getBranchID())
                .merchantID(mb.getMerchant().getMerchantID())
                .branchName(mb.getBranchName())
                .address(mb.getAddress())
                .timezone(mb.getTimezone())
                .createdAt(mb.getCreatedAt())
                .updatedAt(mb.getUpdatedAt())
                .branchWorkingHoursID(mb.getBranchWorkingHoursID())
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
                    .isMultiSelect(iv.getVariety().getIsMultiSelect())
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
        if (vsv.getSubVariety()!= null){
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

    private Specification<Merchant> getMerchantSpecification(SearchMerchantRequest request, String option) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getMerchantID() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("merchantID")),
                        "%" + request.getMerchantID().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }

            if (request.getMerchantName() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("merchantName")),
                        "%" + request.getMerchantName().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }

            if (request.getPicName() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("picName")),
                        "%" + request.getPicName().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }

            if (request.getPicNumber() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("picNumber")),
                        "%" + request.getPicNumber().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }

            if (request.getPicEmail() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("picEmail")),
                        "%" + request.getPicEmail().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }

            if (request.getMerchantDescription() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("merchantDescription")),
                        "%" + request.getMerchantDescription().toLowerCase() + "%"
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

            if (request.getMerchantStatusID() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("merchantStatusID")),
                        "%" + request.getMerchantStatusID().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }

            if (request.getNotes() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("notes")),
                        "%" + request.getNotes().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }

            if (option.equalsIgnoreCase("active")) {
                Predicate predicate = criteriaBuilder.equal(
                        root.get("merchantStatus").get("status"),
                        EMerchantStatus.ACTIVE
                );
                predicates.add(predicate);
            }

            return query
                    .where(predicates.toArray(new Predicate[]{}))
                    .getRestriction();
        };
    }
}
