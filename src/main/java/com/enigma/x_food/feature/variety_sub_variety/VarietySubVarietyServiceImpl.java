package com.enigma.x_food.feature.variety_sub_variety;

import com.enigma.x_food.feature.variety.Variety;
import com.enigma.x_food.feature.variety.VarietyService;
import com.enigma.x_food.feature.variety_sub_variety.dto.request.VarietySubVarietyRequest;
import com.enigma.x_food.feature.variety_sub_variety.dto.response.VarietySubVarietyResponse;
import com.enigma.x_food.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VarietySubVarietyServiceImpl implements VarietySubVarietyService {
    private final VarietySubVarietyRepository varietyRepository;
    private final VarietyService varietyService;
    private final ValidationUtil validationUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public VarietySubVarietyResponse createNew(VarietySubVarietyRequest request) {
        try {
            log.info("Start createNew");
            validationUtil.validate(request);

            Variety variety = varietyService.getById(request.getVarietyID());

            VarietySubVariety varietySubVariety = VarietySubVariety.builder()
                    .variety(variety)
                    .subVariety(request.getSubVariety())
                    .build();
            varietyRepository.saveAndFlush(varietySubVariety);
            log.info("End createNew");
            return mapToResponse(varietySubVariety);
        } catch (DataIntegrityViolationException e) {
            log.error("Error createNew: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone number already exist");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<VarietySubVariety> getAll() {
        return varietyRepository.findAll();
    }

    @Override
    public VarietySubVarietyResponse getById(String id) {
        return mapToResponse(findByIdOrThrowNotFound(id));
    }

    @Override
    public VarietySubVariety findById(String id) {
        return findByIdOrThrowNotFound(id);
    }

    private VarietySubVariety findByIdOrThrowNotFound(String id) {
        return varietyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "variety not found"));
    }

    private VarietySubVarietyResponse mapToResponse(VarietySubVariety varietySubVariety) {
        return VarietySubVarietyResponse.builder()
                .varietySubVarietyID(varietySubVariety.getVarietySubVarietyID())
                .varietyID(varietySubVariety.getVariety().getVarietyID())
                .subVarietyID(varietySubVariety.getSubVariety().getSubVarietyID())
                .build();
    }
}
