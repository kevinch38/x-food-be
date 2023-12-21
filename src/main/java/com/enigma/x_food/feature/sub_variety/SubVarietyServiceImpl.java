package com.enigma.x_food.feature.sub_variety;

import com.enigma.x_food.feature.merchant_branch.MerchantBranch;
import com.enigma.x_food.feature.merchant_branch.MerchantBranchService;
import com.enigma.x_food.feature.sub_variety.dto.request.SubVarietyRequest;
import com.enigma.x_food.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubVarietyServiceImpl implements SubVarietyService {
    private final SubVarietyRepository subVarietyRepository;
    private final ValidationUtil validationUtil;
    private final MerchantBranchService merchantBranchService;
    private final EntityManager entityManager;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public SubVariety createNew(SubVarietyRequest request) {
        try {
            log.info("Start createNew");
            validationUtil.validate(request);

            MerchantBranch merchantBranch = merchantBranchService.findById(request.getBranchID());

            SubVariety subVariety = SubVariety.builder()
                    .subVarName(request.getSubVarName())
                    .merchantBranch(entityManager.merge(merchantBranch))
                    .subVarStock(request.getSubVarStock())
                    .subVarPrice(request.getSubVarPrice())
                    .build();
            subVarietyRepository.saveAndFlush(subVariety);
            log.info("End createNew");
            return subVariety;
        } catch (DataIntegrityViolationException e) {
            log.error("Error createNew: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone number already exist");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubVariety> getAll() {
        return subVarietyRepository.findAll();
    }

    @Override
    public SubVariety getById(String id) {
        return (findByIdOrThrowNotFound(id));
    }

    private SubVariety findByIdOrThrowNotFound(String id) {
        return subVarietyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "subVariety not found"));
    }
}
