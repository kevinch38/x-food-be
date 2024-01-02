package com.enigma.x_food.feature.item_variety;

import com.enigma.x_food.feature.item_variety.dto.response.ItemVarietyResponse;
import com.enigma.x_food.shared.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/item-varieties")
@RequiredArgsConstructor
public class ItemVarietyController {
    private final ItemVarietyService itemVarietyService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<ItemVariety> itemVarieties = itemVarietyService.getAll();

        CommonResponse<List<ItemVariety>> response = CommonResponse.<List<ItemVariety>>builder()
                .message("successfully get all variety")
                .statusCode(HttpStatus.OK.value())
                .data(itemVarieties)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@PathVariable String id) {
        ItemVarietyResponse itemVarietyResponse = itemVarietyService.getById(id);
        CommonResponse<ItemVarietyResponse> response = CommonResponse.<ItemVarietyResponse>builder()
                .message("successfully get itemVariety")
                .statusCode(HttpStatus.OK.value())
                .data(itemVarietyResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
