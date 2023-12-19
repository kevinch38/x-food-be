package com.enigma.x_food.feature.item;

import com.enigma.x_food.feature.item.dto.request.SearchItemRequest;
import com.enigma.x_food.feature.item.dto.response.ItemResponse;
import com.enigma.x_food.shared.CommonResponse;
import com.enigma.x_food.util.PagingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
    public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(required = false, defaultValue = "itemIName") String sortBy) {
        direction = PagingUtil.validateDirection(direction);

        SearchItemRequest request = SearchItemRequest.builder()
                .direction(direction)
                .sortBy(sortBy)
                .build();
        List<ItemResponse> items = itemService.getAll(request);

        CommonResponse<List<ItemResponse>> response = CommonResponse.<List<ItemResponse>>builder()
                .message("successfully get all item")
                .statusCode(HttpStatus.OK.value())
                .data(items)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
