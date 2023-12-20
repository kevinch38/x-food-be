package com.enigma.x_food.feature.merchant_branch_status;

import com.enigma.x_food.constant.EMerchantBranchStatus;
import com.enigma.x_food.feature.merchant_branch_status.response.MerchantBranchStatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MerchantBranchStatusServiceImpl implements MerchantBranchStatusService {
    private final MerchantBranchStatusRepository merchantBranchStatusRepository;

    @Transactional(rollbackFor = Exception.class)
    @PostConstruct
    private void init() {
        List<MerchantBranchStatus> merchantBranchStatus = new ArrayList<>();
        for (EMerchantBranchStatus value : EMerchantBranchStatus.values()) {
            Optional<MerchantBranchStatus> status = merchantBranchStatusRepository.findByStatus(value);
            if (status.isPresent()) continue;

            merchantBranchStatus.add(MerchantBranchStatus.builder()
                    .status(value)
                    .build());
        }
        merchantBranchStatusRepository.saveAllAndFlush(merchantBranchStatus);
    }

    @Transactional(readOnly = true)
    @Override
    public MerchantBranchStatus getByStatus(EMerchantBranchStatus status) {
        return findByStatusOrThrowNotFound(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MerchantBranchStatusResponse> getAll() {
        log.info("Start getAll");

        List<MerchantBranchStatus> merchantBranchStatus = merchantBranchStatusRepository.findAll();
        log.info("End getAll");
        return merchantBranchStatus.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private MerchantBranchStatus findByStatusOrThrowNotFound(EMerchantBranchStatus status) {
        return merchantBranchStatusRepository.findByStatus(status)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "merchant branch status not found"));
    }

    private MerchantBranchStatusResponse mapToResponse(MerchantBranchStatus merchantBranchStatus) {
        return MerchantBranchStatusResponse.builder()
                .merchantBranchStatusID(merchantBranchStatus.getMerchantBranchStatusID())
                .status(merchantBranchStatus.getStatus().toString())
                .createdAt(merchantBranchStatus.getCreatedAt())
                .updatedAt(merchantBranchStatus.getUpdatedAt())
                .build();
    }
}
