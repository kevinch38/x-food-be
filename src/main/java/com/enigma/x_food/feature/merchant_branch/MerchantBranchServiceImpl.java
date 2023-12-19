package com.enigma.x_food.feature.merchant_branch;

import com.enigma.x_food.feature.merchant.Merchant;
import com.enigma.x_food.feature.merchant.MerchantService;
import com.enigma.x_food.feature.merchant.dto.response.MerchantResponse;
import com.enigma.x_food.feature.merchant_branch.dto.request.NewMerchantBranchRequest;
import com.enigma.x_food.feature.merchant_branch.dto.request.SearchMerchantBranchRequest;
import com.enigma.x_food.feature.merchant_branch.dto.request.UpdateMerchantBranchRequest;
import com.enigma.x_food.feature.merchant_branch.dto.response.MerchantBranchResponse;
import com.enigma.x_food.util.SortingUtil;
import com.enigma.x_food.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MerchantBranchServiceImpl implements MerchantBranchService {
    private final MerchantBranchRepository merchantBranchRepository;
    private final MerchantService merchantService;
    private final ValidationUtil validationUtil;
    private final EntityManager entityManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MerchantBranchResponse createNew(NewMerchantBranchRequest request) {
        validationUtil.validate(request);
        MerchantResponse merchantResponse = merchantService.findById(request.getMerchantID());
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
                .merchantStatusID(merchantResponse.getMerchantStatusID())
                .notes(merchantResponse.getNotes())
                .build();

        MerchantBranch branch = MerchantBranch.builder()
                .merchant(entityManager.merge(merchant))
                .branchName(request.getBranchName())
                .address(request.getAddress())
                .timezone(request.getTimezone())
                .branchWorkingHoursID(request.getBranchWorkingHoursID())
                .cityID(request.getCityID())
                .build();

        merchantBranchRepository.saveAndFlush(branch);
        return mapToResponse(branch);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MerchantBranchResponse update(UpdateMerchantBranchRequest request) {
        validationUtil.validate(request);
        MerchantBranch merchantBranch = findByIdOrThrowException(request.getBranchID());

        MerchantBranch updated = MerchantBranch.builder()
                .merchant(merchantBranch.getMerchant())
                .branchName(request.getBranchName())
                .address(request.getAddress())
                .timezone(request.getTimezone())
                .branchWorkingHoursID(request.getBranchWorkingHoursID())
                .cityID(request.getCityID())
                .createdAt(merchantBranch.getCreatedAt())
                .build();

        return mapToResponse(merchantBranchRepository.saveAndFlush(updated));
    }

    @Override
    @Transactional(readOnly = true)
    public MerchantBranch findById(String id) {
        validationUtil.validate(id);
        return findByIdOrThrowException(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MerchantBranchResponse> findByMerchantId(SearchMerchantBranchRequest request) {
        String fieldName = SortingUtil.sortByValidation(MerchantBranch.class, request.getSortBy(), "branchID");
        request.setSortBy(fieldName);

        Specification<MerchantBranch> specification = getMerchantBranchSpecification(request);
        List<MerchantBranch> branches = merchantBranchRepository.findAll(specification);
        return branches.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String id) {
        validationUtil.validate(id);
        MerchantBranch merchantBranch = findByIdOrThrowException(id);
        merchantBranchRepository.delete(merchantBranch);
    }

    private MerchantBranch findByIdOrThrowException(String id) {
        return merchantBranchRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MerchantBranch not found"));
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
                .cityID(branch.getCityID())
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
                        criteriaBuilder.lower(root.get("cityID")),
                        "%" + request.getCityID().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }

            return query
                    .where(predicates.toArray(new Predicate[]{}))
                    .getRestriction();
        };
    }
}
