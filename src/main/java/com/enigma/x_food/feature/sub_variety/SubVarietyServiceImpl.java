package com.enigma.x_food.feature.sub_variety;

import com.enigma.x_food.feature.merchant_branch.MerchantBranch;
import com.enigma.x_food.feature.merchant_branch.MerchantBranchService;
import com.enigma.x_food.feature.sub_variety.dto.request.SubVarietyRequest;
import com.enigma.x_food.feature.sub_variety.dto.response.SubVarietyResponse;
import com.enigma.x_food.feature.variety_sub_variety.VarietySubVarietyService;
import com.enigma.x_food.feature.variety_sub_variety.dto.request.VarietySubVarietyRequest;
import com.enigma.x_food.feature.variety_sub_variety.dto.response.VarietySubVarietyResponse;
import com.enigma.x_food.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubVarietyServiceImpl implements SubVarietyService {
    private final SubVarietyRepository subVarietyRepository;
    private final ValidationUtil validationUtil;
    private final MerchantBranchService merchantBranchService;
    private final VarietySubVarietyService varietySubVarietyService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public SubVarietyResponse createNew(SubVarietyRequest request) {
        log.info("Start createNew");
        validationUtil.validate(request);

        MerchantBranch merchantBranch = merchantBranchService.getById(request.getBranchID());

        SubVariety subVariety = SubVariety.builder()
                .subVarName(request.getSubVarName())
                .merchantBranch(merchantBranch)
                .subVarStock(request.getSubVarStock())
                .subVarPrice(request.getSubVarPrice())
                .build();
        subVarietyRepository.saveAndFlush(subVariety);

        VarietySubVarietyRequest varietySubVarietyRequest = VarietySubVarietyRequest.builder()
                .varietyID(request.getVarietyID())
                .subVariety(subVariety)
                .build();
        varietySubVarietyService.createNew(varietySubVarietyRequest);

        log.info("End createNew");
        return mapToResponse(subVariety);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubVarietyResponse> getAll() {
        return subVarietyRepository.findAll().stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SubVarietyResponse getById(String id) {
        return mapToResponse(findByIdOrThrowNotFound(id));
    }

    private SubVariety findByIdOrThrowNotFound(String id) {
        return subVarietyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "sub variety not found"));
    }

    private SubVarietyResponse mapToResponse(SubVariety subVariety) {
        List<VarietySubVarietyResponse> varietySubVarieties = null;
        if (subVariety.getVarietySubVarieties()!= null){
            varietySubVarieties = subVariety.getVarietySubVarieties().stream().map(
                    vsv -> VarietySubVarietyResponse.builder()
                            .varietySubVarietyID(vsv.getVarietySubVarietyID())
                            .subVarietyID(vsv.getSubVariety().getSubVarietyID())
                            .varietyID(vsv.getVariety().getVarietyID())
                            .build()
                    )
                    .collect(Collectors.toList());
        }

        return SubVarietyResponse.builder()
                .subVarietyID(subVariety.getSubVarietyID())
                .branchID(subVariety.getMerchantBranch().getBranchID())
                .subVarName(subVariety.getSubVarName())
                .subVarStock(subVariety.getSubVarStock())
                .varietySubVariety(varietySubVarieties)
                .build();
    }


}
