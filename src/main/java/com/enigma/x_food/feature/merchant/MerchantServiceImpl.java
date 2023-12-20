package com.enigma.x_food.feature.merchant;

import com.enigma.x_food.constant.EMerchantStatus;
import com.enigma.x_food.feature.merchant.dto.request.NewMerchantRequest;
import com.enigma.x_food.feature.merchant.dto.request.SearchMerchantRequest;
import com.enigma.x_food.feature.merchant.dto.request.UpdateMerchantRequest;
import com.enigma.x_food.feature.merchant.dto.response.MerchantResponse;
import com.enigma.x_food.feature.merchant_status.MerchantStatus;
import com.enigma.x_food.feature.merchant_status.MerchantStatusService;
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
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {
    private final MerchantRepository merchantRepository;
    private final ValidationUtil validationUtil;
    private final EntityManager entityManager;
    private final MerchantStatusService merchantStatusService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MerchantResponse createNew(NewMerchantRequest request) {
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
                .build();
        merchantRepository.saveAndFlush(merchant);
        return mapToResponse(merchant);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MerchantResponse update(UpdateMerchantRequest request) {
        validationUtil.validate(request);
        Merchant merchant = findByIdOrThrowException(request.getMerchantID());

        Merchant updated = Merchant.builder()
                .merchantID(merchant.getMerchantID())
                .joinDate(merchant.getJoinDate())
                .merchantName(request.getMerchantName())
                .picName(request.getPicName())
                .picNumber(request.getPicNumber())
                .picEmail(request.getPicEmail())
                .merchantDescription(request.getMerchantDescription())
                .adminID(merchant.getAdminID())
                .createdAt(merchant.getCreatedAt())
                .merchantStatus(merchant.getMerchantStatus())
                .notes(request.getNotes())
                .build();

        return mapToResponse(merchantRepository.saveAndFlush(updated));
    }

    @Override
    @Transactional(readOnly = true)
    public MerchantResponse findById(String id) {
        validationUtil.validate(id);
        return mapToResponse(findByIdOrThrowException(id));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String id) {
        validationUtil.validate(id);
        Merchant merchant = findByIdOrThrowException(id);
        MerchantStatus merchantStatus = merchantStatusService.getByStatus(EMerchantStatus.INACTIVE);
        merchant.setMerchantStatus(merchantStatus);

        merchantRepository.saveAndFlush(merchant);
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

    private MerchantResponse mapToResponse(Merchant merchant) {
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
                        "%" +request.getMerchantName().toLowerCase() +"%"
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

            return query
                    .where(predicates.toArray(new Predicate[]{}))
                    .getRestriction();
        };
    }
}
