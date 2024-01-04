package com.enigma.x_food.feature.sub_variety;

import com.enigma.x_food.feature.sub_variety.dto.request.SubVarietyRequest;
import com.enigma.x_food.feature.sub_variety.dto.response.SubVarietyResponse;
import com.enigma.x_food.shared.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sub-varieties")
@RequiredArgsConstructor
public class SubVarietyController {
    private final SubVarietyService SubVarietyService;

//    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> createNew(@RequestBody SubVarietyRequest request) {
//        SubVarietyResponse SubVarietyResponse = SubVarietyService.createNew(request);
//        CommonResponse<SubVarietyResponse> response = CommonResponse.<SubVarietyResponse>builder()
//                .message("successfully create new sub variety")
//                .statusCode(HttpStatus.CREATED.value())
//                .data(SubVarietyResponse)
//                .build();
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(response);
//    }
//
//    @GetMapping
//    public ResponseEntity<?> getAll() {
//        List<SubVarietyResponse> varieties = SubVarietyService.getAll();
//
//        CommonResponse<List<SubVarietyResponse>> response = CommonResponse.<List<SubVarietyResponse>>builder()
//                .message("successfully get all sub varieties")
//                .statusCode(HttpStatus.OK.value())
//                .data(varieties)
//                .build();
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(response);
//    }
//
//    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> getById(@PathVariable String id) {
//        SubVarietyResponse SubVarietyResponse = SubVarietyService.findById(id);
//        CommonResponse<SubVarietyResponse> response = CommonResponse.<SubVarietyResponse>builder()
//                .message("successfully get sub variety")
//                .statusCode(HttpStatus.OK.value())
//                .data(SubVarietyResponse)
//                .build();
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(response);
//    }
}
