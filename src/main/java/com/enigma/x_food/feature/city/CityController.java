package com.enigma.x_food.feature.city;

import com.enigma.x_food.feature.city.dto.response.CityResponse;
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
@RequestMapping("/api/cities")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class CityController {
    private final CityService cityService;
    @PreAuthorize("permitAll")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll() {
        List<CityResponse> cityResponse = cityService.getAll();
        CommonResponse<List<CityResponse>> response = CommonResponse.<List<CityResponse>>builder()
                .message("successfully get city")
                .statusCode(HttpStatus.OK.value())
                .data(cityResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
    @PreAuthorize("permitAll")
    @GetMapping(value = "/jabodetabek",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllJabodetabek() {
        List<CityResponse> cityResponse = cityService.getAllJabodetabek();
        CommonResponse<List<CityResponse>> response = CommonResponse.<List<CityResponse>>builder()
                .message("successfully get city")
                .statusCode(HttpStatus.OK.value())
                .data(cityResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'PARTNERSHIP_STAFF', 'PARTNERSHIP_HEAD')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@PathVariable String id) {
        CityResponse cityResponse = cityService.getById(id);
        CommonResponse<CityResponse> response = CommonResponse.<CityResponse>builder()
                .message("successfully get city")
                .statusCode(HttpStatus.OK.value())
                .data(cityResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
