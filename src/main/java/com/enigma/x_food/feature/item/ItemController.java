package com.enigma.x_food.feature.item;

import com.enigma.x_food.feature.item.dto.request.NewItemRequest;
import com.enigma.x_food.feature.item.dto.request.SearchItemRequest;
import com.enigma.x_food.feature.item.dto.response.ItemResponse;
import com.enigma.x_food.shared.CommonResponse;
import com.enigma.x_food.util.PagingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
    public class ItemController {
    private final ItemService itemService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNew(
            @RequestParam String itemName,
            @RequestParam String category,
            @RequestParam String branchID,
            @RequestParam MultipartFile image,
            @RequestParam Double initialPrice,
            @RequestParam Double discountedPrice,
            @RequestParam Integer itemStock,
            @RequestParam Boolean isDiscounted,
            @RequestParam Boolean isRecommended,
            @RequestParam String itemDescription
    ) throws IOException {
        NewItemRequest request = NewItemRequest.builder()
                .itemName(itemName)
                .category(category)
                .branchID(branchID)
                .image(image)
                .initialPrice(initialPrice)
                .discountedPrice(discountedPrice)
                .itemStock(itemStock)
                .isDiscounted(isDiscounted)
                .isRecommended(isRecommended)
                .itemDescription(itemDescription)
                .build();

        ItemResponse itemResponse = itemService.createNew(request);
        CommonResponse<ItemResponse> response = CommonResponse.<ItemResponse>builder()
                .message("successfully create new item")
                .statusCode(HttpStatus.CREATED.value())
                .data(itemResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam String branchID,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(required = false, defaultValue = "itemName") String sortBy) {
        direction = PagingUtil.validateDirection(direction);

        SearchItemRequest request = SearchItemRequest.builder()
                .direction(direction)
                .sortBy(sortBy)
                .branchID(branchID)
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
