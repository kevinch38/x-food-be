package com.enigma.x_food.feature.admin_monitoring;

import com.enigma.x_food.feature.admin_monitoring.dto.request.AdminMonitoringRequest;
import com.enigma.x_food.feature.admin_monitoring.dto.response.AdminMonitoringResponse;
import com.enigma.x_food.feature.admin_monitoring.dto.response.SearchAdminMonitoringRequest;
import com.enigma.x_food.shared.CommonResponse;
import com.enigma.x_food.shared.PagingResponse;
import com.enigma.x_food.util.PagingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin-monitoring")
@RequiredArgsConstructor
public class AdminMonitoringController {
    private final AdminMonitoringService adminMonitoringService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNew(@RequestBody AdminMonitoringRequest request) {
        AdminMonitoringResponse adminMonitoringResponse = adminMonitoringService.createNew(request);
        CommonResponse<AdminMonitoringResponse> response = CommonResponse.<AdminMonitoringResponse>builder()
                .message("successfully create new adminMonitoring")
                .statusCode(HttpStatus.CREATED.value())
                .data(adminMonitoringResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findById(@PathVariable String id) {
        AdminMonitoringResponse adminMonitoringResponse = adminMonitoringService.findById(id);
        CommonResponse<AdminMonitoringResponse> response = CommonResponse.<AdminMonitoringResponse>builder()
                .message("successfully get adminMonitoring")
                .statusCode(HttpStatus.OK.value())
                .data(adminMonitoringResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(required = false, defaultValue = "adminMonitoringID") String sortBy,
            @RequestParam(required = false) String adminName
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
                .build();

        Page<AdminMonitoringResponse> adminMonitoringResponses = adminMonitoringService.findAll(request);

        PagingResponse pagingResponse = PagingResponse.builder()
                .page(page)
                .size(size)
                .count(adminMonitoringResponses.getTotalElements())
                .totalPages(adminMonitoringResponses.getTotalPages())
                .build();

        CommonResponse<List<AdminMonitoringResponse>> response = CommonResponse.<List<AdminMonitoringResponse>>builder()
                .message("successfully get all admin monitoring")
                .statusCode(HttpStatus.OK.value())
                .data(adminMonitoringResponses.getContent())
                .paging(pagingResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
