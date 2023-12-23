package com.enigma.x_food.feature.item_variety;

import com.enigma.x_food.feature.item.Item;
import com.enigma.x_food.feature.item.ItemService;
import com.enigma.x_food.feature.item_variety.dto.request.ItemVarietyRequest;
import com.enigma.x_food.feature.item_variety.dto.response.ItemVarietyResponse;
import com.enigma.x_food.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemVarietyServiceImpl implements ItemVarietyService {
    private final ItemVarietyRepository varietyRepository;
    private final ItemService itemService;
    private final ValidationUtil validationUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ItemVarietyResponse createNew(ItemVarietyRequest request) {
        log.info("Start createNew");
        validationUtil.validate(request);

        Item item = itemService.findById(request.getItemID());

        ItemVariety varietySubVariety = ItemVariety.builder()
                .item(item)
                .variety(request.getVariety())
                .build();

        varietyRepository.save(varietySubVariety);
        log.info("End createNew");
        return mapToResponse(varietySubVariety);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemVariety> getAll() {
        return varietyRepository.findAll();
    }

    @Override
    public ItemVarietyResponse getById(String id) {
        return mapToResponse(findByIdOrThrowNotFound(id));
    }

    private ItemVariety findByIdOrThrowNotFound(String id) {
        return varietyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "variety not found"));
    }

    private ItemVarietyResponse mapToResponse(ItemVariety itemVariety) {
        return ItemVarietyResponse.builder()
                .itemVarietyID(itemVariety.getItemVarietyID())
                .variety(itemVariety.getVariety())
                .build();
    }
}
