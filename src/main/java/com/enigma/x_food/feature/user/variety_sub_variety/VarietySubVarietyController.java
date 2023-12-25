package com.enigma.x_food.feature.user.variety_sub_variety;

import com.enigma.x_food.feature.user.variety_sub_variety.dto.response.VarietySubVarietyResponse;
import com.enigma.x_food.shared.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/variety-sub-varieties")
@RequiredArgsConstructor
public class VarietySubVarietyController {
    private final VarietySubVarietyService varietyService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<VarietySubVariety> varieties = varietyService.getAll();

        CommonResponse<List<VarietySubVariety>> response = CommonResponse.<List<VarietySubVariety>>builder()
                .message("successfully get all variety")
                .statusCode(HttpStatus.OK.value())
                .data(varieties)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@PathVariable String id) {
        VarietySubVarietyResponse varietyResponse = varietyService.getById(id);
        CommonResponse<VarietySubVarietyResponse> response = CommonResponse.<VarietySubVarietyResponse>builder()
                .message("successfully get variety")
                .statusCode(HttpStatus.OK.value())
                .data(varietyResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
