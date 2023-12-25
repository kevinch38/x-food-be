package com.enigma.x_food.feature.item;

import com.enigma.x_food.constant.ECategory;
import com.enigma.x_food.feature.category.Category;
import com.enigma.x_food.feature.category.CategoryService;
import com.enigma.x_food.feature.item.dto.request.NewItemRequest;
import com.enigma.x_food.feature.item.dto.request.SearchItemRequest;
import com.enigma.x_food.feature.item.dto.response.ItemResponse;
import com.enigma.x_food.feature.merchant_branch.MerchantBranch;
import com.enigma.x_food.feature.merchant_branch.MerchantBranchService;
import com.enigma.x_food.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.Predicate;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final MerchantBranchService merchantBranchService;
    private final ValidationUtil validationUtil;
    private final CategoryService categoryService;

    @Override
    @Transactional(readOnly = true)
    public Item findById(String id) {
        validationUtil.validate(id);
        return findByIdOrThrowException(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemResponse> getAll(SearchItemRequest request) {
        validationUtil.validate(request);

        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());

        Specification<Item> specification = getItemSpecification(request);
        List<Item> items = itemRepository.findAll(specification, sort);
        return items.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public ItemResponse createNew(NewItemRequest request) {
        try {
            validationUtil.validate(request);

            MerchantBranch merchantBranch = merchantBranchService.getById(request.getBranchID());
            Category category = categoryService.getByCategoryName(ECategory.valueOf(request.getCategory()));

            Item item = Item.builder()
                    .itemName(request.getItemName())
                    .category(category)
                    .merchantBranch(merchantBranch)
                    .image(request.getImage().getBytes())
                    .initialPrice(request.getInitialPrice())
                    .discountedPrice(request.getDiscountedPrice())
                    .itemStock(request.getItemStock())
                    .isDiscounted(request.getIsDiscounted())
                    .isRecommended(request.getIsRecommended())
                    .itemDescription(request.getItemDescription())
                    .build();
            itemRepository.saveAndFlush(item);

            log.info("End createNew");
            return mapToResponse(item);
        } catch (DataIntegrityViolationException e) {
            log.error("Error createNew: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone number already exist");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
                .category(item.getCategory().getCategoryName().name())
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

    private Specification<Item> getItemSpecification(SearchItemRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getBranchID() != null) {
                Predicate predicate = criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("merchantBranch").get("branchID")),
                        request.getBranchID().toLowerCase()
                );
                predicates.add(predicate);
            }

            return query
                    .where(predicates.toArray(new Predicate[]{}))
                    .getRestriction();
        };
    }
}
