package com.enigma.x_food.feature.variety;

import com.enigma.x_food.feature.item_variety.ItemVarietyService;
import com.enigma.x_food.feature.item_variety.dto.request.ItemVarietyRequest;
import com.enigma.x_food.feature.variety.dto.request.VarietyRequest;
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
public class VarietyServiceImpl implements VarietyService {
    private final VarietyRepository varietyRepository;
    private final ItemVarietyService itemVarietyService;
    private final ValidationUtil validationUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Variety createNew(VarietyRequest request) {
        log.info("Start createNew");
        validationUtil.validate(request);

        Variety variety = Variety.builder()
                .varietyName(request.getVarietyName())
                .isRequired(request.getIsRequired())
                .isMultiSelect(request.getIsMultiSelect())
                .build();
        varietyRepository.saveAndFlush(variety);

        ItemVarietyRequest itemVarietyRequest = ItemVarietyRequest.builder()
                .itemID(request.getItemID())
                .variety(variety)
                .build();
        itemVarietyService.createNew(itemVarietyRequest);

        log.info("End createNew");
        return variety;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Variety> getAll() {
        return varietyRepository.findAll();
    }

    @Override
    public Variety getById(String id) {
        return (findByIdOrThrowNotFound(id));
    }

    private Variety findByIdOrThrowNotFound(String id) {
        return varietyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "variety not found"));
    }
}
