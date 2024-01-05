package com.enigma.x_food.feature.variety;

import com.enigma.x_food.feature.variety.dto.request.VarietyRequest;
import com.enigma.x_food.shared.CommonResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/varieties")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class VarietyController {
    private final VarietyService varietyService;

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestBody VarietyRequest request) {
        Variety varietyResponse = varietyService.createNew(request);
        CommonResponse<Variety> response = CommonResponse.<Variety>builder()
                .message("successfully create new variety")
                .statusCode(HttpStatus.CREATED.value())
                .data(varietyResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PreAuthorize("permitAll")
    @GetMapping
    public ResponseEntity<?> getAll() {
        List<Variety> varieties = varietyService.getAll();

        CommonResponse<List<Variety>> response = CommonResponse.<List<Variety>>builder()
                .message("successfully get all variety")
                .statusCode(HttpStatus.OK.value())
                .data(varieties)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PreAuthorize("permitAll")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@PathVariable String id) {
        Variety varietyResponse = varietyService.getById(id);
        CommonResponse<Variety> response = CommonResponse.<Variety>builder()
                .message("successfully get variety")
                .statusCode(HttpStatus.OK.value())
                .data(varietyResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
