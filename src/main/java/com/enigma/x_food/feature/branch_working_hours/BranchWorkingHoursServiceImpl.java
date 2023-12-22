package com.enigma.x_food.feature.branch_working_hours;

import com.enigma.x_food.feature.branch_working_hours.dto.request.NewBranchWorkingHoursRequest;
import com.enigma.x_food.feature.branch_working_hours.dto.request.UpdateBranchWorkingHoursRequest;
import com.enigma.x_food.feature.branch_working_hours.dto.response.BranchWorkingHoursResponse;
import com.enigma.x_food.feature.merchant_branch.MerchantBranch;
import com.enigma.x_food.feature.merchant_branch.MerchantBranchService;
import com.enigma.x_food.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;

@Service
@RequiredArgsConstructor
@Slf4j
public class BranchWorkingHoursServiceImpl implements BranchWorkingHoursService {
    private final BranchWorkingHoursRepository branchWorkingHoursRepository;
    private final MerchantBranchService merchantBranchService;
    private final EntityManager entityManager;
    private final ValidationUtil validationUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BranchWorkingHours createNew(NewBranchWorkingHoursRequest request) {
        try {
            log.info("Start createNew");
            validationUtil.validate(request);

            MerchantBranch merchantBranch = merchantBranchService.findById(request.getMerchantBranchID());

            BranchWorkingHours branchWorkingHours = BranchWorkingHours.builder()
                    .openHour(request.getOpenHour())
                    .closeHour(request.getCloseHour())
                    .days(request.getDays())
                    .merchantBranch(entityManager.merge(merchantBranch))
                    .build();

            branchWorkingHoursRepository.save(branchWorkingHours);
            log.info("End createNew");
            return branchWorkingHours;
        } catch (DataIntegrityViolationException e) {
            log.error("Error createNew: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "branchWorkingHours already exist");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BranchWorkingHoursResponse update(UpdateBranchWorkingHoursRequest request) {
        validationUtil.validate(request);
        BranchWorkingHours branchWorkingHours = findByIdOrThrowException(request.getBranchWorkingHoursID());
        branchWorkingHours.setCloseHour(request.getCloseHour());
        branchWorkingHours.setOpenHour(request.getOpenHour());
        branchWorkingHours.setDays(request.getDays());

        return mapToResponse(branchWorkingHoursRepository.saveAndFlush(branchWorkingHours));
    }

    @Override
    @Transactional(readOnly = true)
    public BranchWorkingHoursResponse findById(String id) {
        validationUtil.validate(id);
        return mapToResponse(findByIdOrThrowException(id));
    }

    private BranchWorkingHours findByIdOrThrowException(String id) {
        return branchWorkingHoursRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "BranchWorkingHours not found"));
    }

    private BranchWorkingHoursResponse mapToResponse(BranchWorkingHours branchWorkingHours) {
        return BranchWorkingHoursResponse.builder()
                .branchWorkingHoursID(branchWorkingHours.getBranchWorkingHoursID())
                .openHour(branchWorkingHours.getOpenHour())
                .closeHour(branchWorkingHours.getCloseHour())
                .days(branchWorkingHours.getDays())
                .merchantBranchID(branchWorkingHours.getMerchantBranch().getBranchWorkingHoursID())
                .createdAt(branchWorkingHours.getCreatedAt())
                .updatedAt(branchWorkingHours.getUpdatedAt())
                .build();
    }
}
