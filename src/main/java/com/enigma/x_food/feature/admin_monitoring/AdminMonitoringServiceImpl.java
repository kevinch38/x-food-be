package com.enigma.x_food.feature.admin_monitoring;

import com.enigma.x_food.constant.EActivity;
import com.enigma.x_food.feature.activity.Activity;
import com.enigma.x_food.feature.activity.ActivityService;
import com.enigma.x_food.feature.admin.Admin;
import com.enigma.x_food.feature.admin.AdminService;
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
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminMonitoringServiceImpl implements AdminMonitoringService {
    private final AdminMonitoringRepository adminMonitoringRepository;
    private final ValidationUtil validationUtil;
    private final ActivityService activityService;
    private final AdminService adminService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AdminMonitoringResponse createNew(AdminMonitoringRequest request) {
        validationUtil.validate(request);

        Activity activity = activityService.findByActivity(EActivity.valueOf(request.getActivity()));

        Admin admin = adminService.getById(request.getAdminID());
        AdminMonitoring adminMonitoring = AdminMonitoring.builder()
                .activity(activity)
                .admin(admin)
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
                .activity(adminMonitoring.getActivity().getActivity().name())
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

            return query
                    .where(predicates.toArray(new Predicate[]{}))
                    .getRestriction();
        };
    }
}
