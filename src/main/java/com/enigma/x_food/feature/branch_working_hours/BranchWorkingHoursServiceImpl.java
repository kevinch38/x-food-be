package com.enigma.x_food.feature.branch_working_hours;

import com.enigma.x_food.feature.branch_working_hours.dto.request.NewBranchWorkingHoursRequest;
import com.enigma.x_food.feature.branch_working_hours.dto.request.UpdateBranchWorkingHoursRequest;
import com.enigma.x_food.feature.branch_working_hours.dto.response.BranchWorkingHoursResponse;
import com.enigma.x_food.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BranchWorkingHoursServiceImpl implements BranchWorkingHoursService {
    private final BranchWorkingHoursRepository branchWorkingHoursRepository;
    private final ValidationUtil validationUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<BranchWorkingHours> createNew(List<NewBranchWorkingHoursRequest> request) {
        try {
            log.info("Start createNew");
            validationUtil.validate(request);

            List<BranchWorkingHours> branchWorkingHours = new ArrayList<>();
            for (NewBranchWorkingHoursRequest newBranchWorkingHoursRequest : request) {
                BranchWorkingHours branchWorkingHour = BranchWorkingHours.builder()
                        .openHour(newBranchWorkingHoursRequest.getOpenHour())
                        .closeHour(newBranchWorkingHoursRequest.getCloseHour())
                        .days(DayOfWeek.valueOf(newBranchWorkingHoursRequest.getDays()))
                        .build();
                branchWorkingHours.add(branchWorkingHour);
            }

            List<BranchWorkingHours> branchWorkingHoursList = branchWorkingHoursRepository.saveAll(branchWorkingHours);
            log.info("End createNew");
            return branchWorkingHoursList;
        } catch (DataIntegrityViolationException e) {
            log.error("Error createNew: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "branchWorkingHours already exist");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<BranchWorkingHoursResponse> update(List<UpdateBranchWorkingHoursRequest> request) {
        validationUtil.validate(request);
        List<BranchWorkingHours> branchWorkingHours = new ArrayList<>();
        for (UpdateBranchWorkingHoursRequest updateBranchWorkingHoursRequest : request) {
            BranchWorkingHours branchWorkingHour = findByIdOrThrowException(updateBranchWorkingHoursRequest.getBranchWorkingHoursID());
            branchWorkingHour.setCloseHour(updateBranchWorkingHoursRequest.getCloseHour());
            branchWorkingHour.setOpenHour(updateBranchWorkingHoursRequest.getOpenHour());
            branchWorkingHour.setDays(DayOfWeek.valueOf(updateBranchWorkingHoursRequest.getDays()));

            branchWorkingHours.add(branchWorkingHour);
        }

        return branchWorkingHoursRepository.saveAllAndFlush(branchWorkingHours).stream().map(
                this::mapToResponse
        ).collect(Collectors.toList());
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
                .days(branchWorkingHours.getDays().name())
                .createdAt(branchWorkingHours.getCreatedAt())
                .updatedAt(branchWorkingHours.getUpdatedAt())
                .build();
    }
}
