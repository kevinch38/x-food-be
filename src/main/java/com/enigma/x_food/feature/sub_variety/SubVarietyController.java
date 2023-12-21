package com.enigma.x_food.feature.sub_variety;

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
@RequestMapping("/api/sub-varieties")
@RequiredArgsConstructor
public class SubVarietyController {
    private final SubVarietyService SubVarietyService;
//
//    @PostMapping(path = "/register",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> createNewSubVariety(@RequestBody SubVariety request) {
//        SubVariety SubVarietyResponse = SubVarietyService.createNew(request);
//        CommonResponse<SubVariety> response = CommonResponse.<SubVariety>builder()
//                .message("successfully create new SubVariety")
//                .statusCode(HttpStatus.CREATED.value())
//                .data(SubVarietyResponse)
//                .build();
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(response);
//    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<SubVariety> varieties = SubVarietyService.getAll();

        CommonResponse<List<SubVariety>> response = CommonResponse.<List<SubVariety>>builder()
                .message("successfully get all sub varieties")
                .statusCode(HttpStatus.OK.value())
                .data(varieties)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@PathVariable String id) {
        SubVariety SubVarietyResponse = SubVarietyService.getById(id);
        CommonResponse<SubVariety> response = CommonResponse.<SubVariety>builder()
                .message("successfully get sub variety")
                .statusCode(HttpStatus.OK.value())
                .data(SubVarietyResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
