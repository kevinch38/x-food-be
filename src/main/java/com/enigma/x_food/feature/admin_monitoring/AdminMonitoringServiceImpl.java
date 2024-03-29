package com.enigma.x_food.feature.admin_monitoring;

import com.enigma.x_food.constant.EActivity;
import com.enigma.x_food.constant.ERole;
import com.enigma.x_food.feature.admin_monitoring.dto.request.AdminMonitoringRequest;
import com.enigma.x_food.feature.admin_monitoring.dto.response.AdminMonitoringResponse;
import com.enigma.x_food.feature.admin_monitoring.dto.request.SearchAdminMonitoringRequest;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.Predicate;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminMonitoringServiceImpl implements AdminMonitoringService {
    private final AdminMonitoringRepository adminMonitoringRepository;
    private final ValidationUtil validationUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AdminMonitoringResponse createNew(AdminMonitoringRequest request) {
        validationUtil.validate(request);

        AdminMonitoring adminMonitoring = AdminMonitoring.builder()
                .activity(EActivity.valueOf(request.getActivity()).name())
                .admin(request.getAdmin())
                .build();

        return mapToResponse(adminMonitoringRepository.saveAndFlush(adminMonitoring));
    }

    @Transactional(readOnly = true)
    @Override
    public AdminMonitoringResponse findById(String id) {
        return mapToResponse(findByIdOrThrowNotFound(id));
    }

    @Transactional(readOnly = true)
    @Override
    public AdminMonitoring getById(String id) {
        return findByIdOrThrowNotFound(id);
    }

    @Override
    public Page<AdminMonitoringResponse> findAll(SearchAdminMonitoringRequest request) {
        String fieldName = SortingUtil.sortByValidation(AdminMonitoring.class, request.getSortBy(), "adminMonitoringID");
        request.setSortBy(fieldName);

        Sort.Direction direction = Sort.Direction.fromString(request.getDirection());
        Specification<AdminMonitoring> specification = getAdminMonitoringSpecification(request);

        Pageable pageable = PageRequest.of(
                request.getPage() - 1,
                request.getSize(),
                direction,
                request.getSortBy()
        );

        Page<AdminMonitoring> adminMonitoring = adminMonitoringRepository.findAll(specification, pageable);
        return adminMonitoring.map(this::mapToResponse);
    }


    private AdminMonitoringResponse mapToResponse(AdminMonitoring adminMonitoring) {
        return AdminMonitoringResponse.builder()
                .adminMonitoringID(adminMonitoring.getAdminMonitoringID())
                .activity(adminMonitoring.getActivity())
                .adminName(adminMonitoring.getAdmin().getAdminName())
                .adminRole(adminMonitoring.getAdmin().getRole().getRole().name())
                .adminID(adminMonitoring.getAdmin().getAdminID())
                .adminEmail(adminMonitoring.getAdmin().getAdminEmail())
                .createdAt(adminMonitoring.getCreatedAt())
                .updatedAt(adminMonitoring.getUpdatedAt())
                .build();
    }

    private AdminMonitoring findByIdOrThrowNotFound(String id) {
        return adminMonitoringRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "adminMonitoring not found"));
    }

    private Specification<AdminMonitoring> getAdminMonitoringSpecification(SearchAdminMonitoringRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getAdminName() != null) {
                Predicate predicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("admin").get("adminName")),
                        "%" + request.getAdminName().toLowerCase() + "%"
                );
                predicates.add(predicate);
            }

            if (request.getAdminRole() != null) {
                Predicate predicate = criteriaBuilder.equal(
                        root.get("admin").get("role").get("role"),
                        ERole.valueOf(request.getAdminRole())
                );
                predicates.add(predicate);
            }

            else if (request.getActivity() != null) {
                Predicate predicate = criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("activity")),
                        request.getActivity().toLowerCase()
                );
                predicates.add(predicate);
            }

            else if (request.getStartUpdatedAt() != null && request.getEndUpdatedAt() != null) {
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

            return query
                    .where(predicates.toArray(new Predicate[]{}))
                    .getRestriction();
        };
    }
}
