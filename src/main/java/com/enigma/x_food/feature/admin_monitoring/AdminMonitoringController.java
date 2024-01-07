package com.enigma.x_food.feature.admin_monitoring;

import com.enigma.x_food.feature.admin_monitoring.dto.request.SearchAdminMonitoringRequest;
import com.enigma.x_food.feature.admin_monitoring.dto.response.AdminMonitoringResponse;
import com.enigma.x_food.shared.CommonResponse;
import com.enigma.x_food.shared.PagingResponse;
import com.enigma.x_food.util.PagingUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin-monitoring")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SUPER_ADMIN')")
@SecurityRequirement(name = "Bearer Authentication")
public class AdminMonitoringController {
    private final AdminMonitoringService adminMonitoringService;

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(required = false, defaultValue = "adminMonitoringID") String sortBy,
            @RequestParam(required = false) String adminName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startUpdatedAt,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endUpdatedAt,
            @RequestParam(required = false) String adminRole,
            @RequestParam(required = false) String activity
    ) {
        page = PagingUtil.validatePage(page);
        size = PagingUtil.validateSize(size);
        direction = PagingUtil.validateDirection(direction);

        SearchAdminMonitoringRequest request = SearchAdminMonitoringRequest.builder()
                .page(page)
                .size(size)
                .direction(direction)
                .sortBy(sortBy)
                .adminName(adminName)
                .startUpdatedAt(startUpdatedAt)
                .endUpdatedAt(endUpdatedAt)
                .adminRole(adminRole)
                .activity(activity)
                .build();
        Page<AdminMonitoringResponse> histories = adminMonitoringService.findAll(request);

        PagingResponse pagingResponse = PagingResponse.builder()
                .page(page)
                .size(size)
                .count(histories.getTotalElements())
                .totalPages(histories.getTotalPages())
                .build();

        CommonResponse<List<AdminMonitoringResponse>> response = CommonResponse.<List<AdminMonitoringResponse>>builder()
                .message("successfully get all histories")
                .statusCode(HttpStatus.OK.value())
                .data(histories.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
