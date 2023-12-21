package com.enigma.x_food.feature.merchant_branch;

import com.enigma.x_food.constant.EMerchantBranchStatus;
import com.enigma.x_food.constant.EMerchantStatus;
import com.enigma.x_food.feature.city.City;
import com.enigma.x_food.feature.city.CityService;
import com.enigma.x_food.feature.city.dto.response.CityResponse;
import com.enigma.x_food.feature.merchant.Merchant;
import com.enigma.x_food.feature.merchant.MerchantService;
import com.enigma.x_food.feature.merchant.dto.response.MerchantResponse;
import com.enigma.x_food.feature.merchant_branch.dto.request.NewMerchantBranchRequest;
import com.enigma.x_food.feature.merchant_branch.dto.request.SearchMerchantBranchRequest;
import com.enigma.x_food.feature.merchant_branch.dto.request.UpdateMerchantBranchRequest;
import com.enigma.x_food.feature.merchant_branch.dto.response.MerchantBranchResponse;
import com.enigma.x_food.feature.merchant_branch_status.MerchantBranchStatus;
import com.enigma.x_food.feature.merchant_branch_status.MerchantBranchStatusService;
import com.enigma.x_food.feature.merchant_status.MerchantStatus;
import com.enigma.x_food.feature.merchant_status.MerchantStatusService;
import com.enigma.x_food.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MerchantBranchServiceImpl implements MerchantBranchService {
    private final MerchantBranchRepository merchantBranchRepository;
    private final MerchantService merchantService;
    private final MerchantStatusService merchantStatusService;
    private final MerchantBranchStatusService merchantBranchStatusService;
    private final ValidationUtil validationUtil;
    private final EntityManager entityManager;
    private final CityService cityService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MerchantBranchResponse createNew(NewMerchantBranchRequest request) throws IOException {
        validationUtil.validate(request);
        MerchantResponse merchantResponse = merchantService.findById(request.getMerchantID());
        MerchantStatus merchantStatus = merchantStatusService.getByStatus(EMerchantStatus.valueOf(merchantResponse.getStatus()));

        Merchant merchant = Merchant.builder()
                .merchantID(merchantResponse.getMerchantID())
                .joinDate(merchantResponse.getJoinDate())
                .merchantName(merchantResponse.getMerchantName())
                .picName(merchantResponse.getPicName())
                .picNumber(merchantResponse.getPicNumber())
                .picEmail(merchantResponse.getPicEmail())
                .merchantDescription(merchantResponse.getMerchantDescription())
                .adminID(merchantResponse.getAdminId())
                .createdAt(merchantResponse.getCreatedAt())
                .updatedAt(merchantResponse.getUpdatedAt())
                .merchantStatus(entityManager.merge(merchantStatus))
                .notes(merchantResponse.getNotes())
                .picName(request.getPicName())
                .picNumber(merchantResponse.getPicNumber())
                .picEmail(merchantResponse.getPicEmail())
                .image(merchantResponse.getImage())
                .build();

        CityResponse cityResponse = cityService.getById(request.getCityID());

        MerchantBranchStatus merchantBranchStatus = merchantBranchStatusService.getByStatus(EMerchantBranchStatus.ACTIVE);
        MerchantBranch branch = MerchantBranch.builder()
                .merchant(entityManager.merge(merchant))
                .branchName(request.getBranchName())
                .address(request.getAddress())
                .timezone(request.getTimezone())
                .branchWorkingHoursID(request.getBranchWorkingHoursID())
                .picName(request.getPicName())
                .picNumber(request.getPicNumber())
                .picEmail(request.getPicEmail())
                .image(request.getImage().getBytes())
                .city(City.builder()
                        .cityID(cityResponse.getCityID())
                        .cityName(cityResponse.getCityName())
                        .build())
                .merchantBranchStatus(entityManager.merge(merchantBranchStatus))
                .joinDate(request.getJoinDate())
                .build();

        merchantBranchRepository.saveAndFlush(branch);
        return mapToResponse(branch);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MerchantBranchResponse update(UpdateMerchantBranchRequest request) throws IOException {
        validationUtil.validate(request);
        MerchantBranch merchantBranch = findByIdOrThrowException(request.getBranchID());

        CityResponse cityResponse = cityService.getById(request.getCityID().isBlank() ? merchantBranch.getCity().getCityID() : request.getCityID());

        merchantBranch.setBranchName(request.getBranchName().isBlank() ? merchantBranch.getBranchName() : request.getBranchName());
        merchantBranch.setAddress(request.getAddress().isBlank() ? merchantBranch.getAddress() : request.getAddress());
        merchantBranch.setTimezone(request.getTimezone().isBlank() ? merchantBranch.getTimezone() : request.getTimezone());
        merchantBranch.setBranchWorkingHoursID(request.getBranchWorkingHoursID().isBlank() ? merchantBranch.getBranchWorkingHoursID() : request.getBranchWorkingHoursID());
        merchantBranch.setPicName(request.getPicName().isBlank() ? merchantBranch.getPicName() : request.getPicName());
        merchantBranch.setPicNumber(request.getPicNumber().isBlank() ? merchantBranch.getPicNumber() : request.getPicNumber());
        merchantBranch.setPicEmail(request.getPicEmail().isBlank() ? merchantBranch.getPicEmail() : request.getPicEmail());
        merchantBranch.setImage(request.getImage().isEmpty() ? merchantBranch.getImage() : request.getImage().getBytes());
        merchantBranch.setCity(City.builder()
                .cityID(cityResponse.getCityID())
                .cityName(cityResponse.getCityName())
                .build());

        return mapToResponse(merchantBranchRepository.saveAndFlush(merchantBranch));
    }

    @Override
    @Transactional(readOnly = true)
    public MerchantBranch findById(String id) {
        validationUtil.validate(id);
        return findByIdOrThrowException(id);
    }

//    @Override
//    @Transactional(readOnly = true)
//    public List<MerchantBranchResponse> findAllByMerchantId(SearchMerchantBranchRequest request) {
//        validationUtil.validate(request);
//        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());
//
//        Specification<MerchantBranch> specification = getMerchantBranchSpecification(request, "all");
//        List<MerchantBranch> branches = merchantBranchRepository.findAll(specification, sort);
//        return branches.stream().map(this::mapToResponse).collect(Collectors.toList());
//    }

    @Override
    @Transactional(readOnly = true)
    public List<MerchantBranchResponse> getAllActiveByMerchantId(SearchMerchantBranchRequest request) {
        validationUtil.validate(request);
        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());

        Specification<MerchantBranch> specification = getMerchantBranchSpecification(request);
        List<MerchantBranch> branches = merchantBranchRepository.findAll(specification, sort);
        return branches.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String id) {
        validationUtil.validate(id);
        MerchantBranch merchantBranch = findByIdOrThrowException(id);
        MerchantBranchStatus merchantBranchStatus = merchantBranchStatusService.getByStatus(EMerchantBranchStatus.INACTIVE);

        merchantBranch.setMerchantBranchStatus(merchantBranchStatus);

        merchantBranchRepository.saveAndFlush(merchantBranch);
    }

    private MerchantBranch findByIdOrThrowException(String id) {
        return merchantBranchRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Merchant branch not found"));
    }

    private MerchantBranchResponse mapToResponse(MerchantBranch branch) {
        return MerchantBranchResponse.builder()
                .branchID(branch.getBranchID())
                .merchantID(branch.getMerchant().getMerchantID())
                .branchName(branch.getBranchName())
                .address(branch.getAddress())
                .timezone(branch.getTimezone())
                .createdAt(branch.getCreatedAt())
                .updatedAt(branch.getUpdatedAt())
                .branchWorkingHoursID(branch.getBranchWorkingHoursID())
                .city(branch.getCity().getCityName())
                .status(branch.getMerchantBranchStatus().getStatus().name())
                .itemList(branch.getItemList())
                .picName(branch.getPicName())
                .picNumber(branch.getPicNumber())
                .picEmail(branch.getPicEmail())
                .image(branch.getImage())
                .joinDate(branch.getJoinDate())
                .build();
    }

    private Specification<MerchantBranch> getMerchantBranchSpecification(SearchMerchantBranchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getBranchID() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("branchID")),
                        "%" + request.getBranchID().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }

            if (request.getMerchantID() != null) {
                Join<MerchantBranch, Merchant> merchantJoin = root.join("merchant", JoinType.INNER);
                Predicate predicate = criteriaBuilder.equal(
                        criteriaBuilder.lower(merchantJoin.get("merchantID")),
                        request.getMerchantID().toLowerCase()
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

            if (request.getAddress() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("address")),
                        "%" + request.getAddress().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }

            if (request.getTimezone() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("timezone")),
                        "%" + request.getTimezone().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }

            if (request.getBranchWorkingHoursID() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("branchWorkingHoursID")),
                        "%" + request.getBranchWorkingHoursID().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }
            if (request.getCityID() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("city").get("cityID")),
                        "%" + request.getCityID().toLowerCase() + "%"
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

            if (request.getPicEmail() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("picEmail")),
                        "%" + request.getPicEmail().toLowerCase() + "%"
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
