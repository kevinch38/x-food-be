package com.enigma.x_food.feature.variety;

import com.enigma.x_food.shared.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/varieties")
@RequiredArgsConstructor
public class VarietyController {
    private final VarietyService varietyService;
//
//    @PostMapping(path = "/register",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> createNewVariety(@RequestBody Variety request) {
//        Variety varietyResponse = varietyService.createNew(request);
//        CommonResponse<Variety> response = CommonResponse.<Variety>builder()
//                .message("successfully create new variety")
//                .statusCode(HttpStatus.CREATED.value())
//                .data(varietyResponse)
//                .build();
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(response);
//    }


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
