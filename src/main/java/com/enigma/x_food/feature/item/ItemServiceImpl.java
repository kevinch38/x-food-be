package com.enigma.x_food.feature.item;

import com.enigma.x_food.feature.item.dto.request.SearchItemRequest;
import com.enigma.x_food.feature.item.dto.response.ItemResponse;
import com.enigma.x_food.feature.merchant_branch.MerchantBranch;
import com.enigma.x_food.util.SortingUtil;
import com.enigma.x_food.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ValidationUtil validationUtil;

    @Override
    @Transactional(readOnly = true)
    public ItemResponse findById(String id) {
        validationUtil.validate(id);
        return mapToResponse(findByIdOrThrowException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemResponse> getAll(SearchItemRequest request) {
        validationUtil.validate(request);

        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());
        List<Item> items = itemRepository.findAll(sort);
        return items.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String id) {
        validationUtil.validate(id);
        Item item = findByIdOrThrowException(id);
        itemRepository.delete(item);
    }

    private Item findByIdOrThrowException(String id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
    }

    private ItemResponse mapToResponse(Item item) {
        return ItemResponse.builder()
                .itemID(item.getItemID())
                .itemName(item.getItemName())
                .categoryID(item.getCategoryID())
                .branchID(item.getMerchantBranch().getBranchID())
                .image(item.getImage())
                .initialPrice(item.getInitialPrice())
                .discountedPrice(item.getDiscountedPrice())
                .itemStock(item.getItemStock())
                .isDiscounted(item.getIsDiscounted())
                .isRecommended(item.getIsRecommended())
                .itemDescription(item.getItemDescription())
                .build();
    }
}
