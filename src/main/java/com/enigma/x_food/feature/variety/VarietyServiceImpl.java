package com.enigma.x_food.feature.variety;

import com.enigma.x_food.security.BCryptUtil;
import com.enigma.x_food.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class VarietyServiceImpl implements VarietyService {
    private final VarietyRepository varietyRepository;
    private final ValidationUtil validationUtil;
    private final Random random;
    private final BCryptUtil bCryptUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Variety createNew(Variety request) {
        try {
            log.info("Start createNew");
            validationUtil.validate(request);

            Variety variety = Variety.builder()
                    .varietyName(request.getVarietyName())
                    .isRequired(request.getIsRequired())
                    .isMultiSelect(request.getIsMultiSelect())
                    .build();
            varietyRepository.saveAndFlush(variety);
            log.info("End createNew");
            return variety;
        } catch (DataIntegrityViolationException e) {
            log.error("Error createNew: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone number already exist");
        }
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
