package com.enigma.x_food.feature.merchant_status;

import com.enigma.x_food.constant.EMerchantStatus;
import com.enigma.x_food.feature.merchant_status.response.MerchantStatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MerchantStatusServiceImpl implements MerchantStatusService {
    private final MerchantStatusRepository merchantStatusRepository;

    @Transactional(rollbackFor = Exception.class)
    @PostConstruct
    private void init() {
        List<MerchantStatus> merchantStatus = new ArrayList<>();
        for (EMerchantStatus value : EMerchantStatus.values()) {
            Optional<MerchantStatus> status = merchantStatusRepository.findByStatus(value);
            if (status.isPresent()) continue;

            merchantStatus.add(MerchantStatus.builder()
                    .status(value)
                    .build());
        }
        merchantStatusRepository.saveAllAndFlush(merchantStatus);
    }

    @Transactional(readOnly = true)
    @Override
    public MerchantStatus getByStatus(EMerchantStatus status) {
        return findByStatusOrThrowNotFound(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MerchantStatusResponse> getAll() {
        log.info("Start getAll");

        List<MerchantStatus> merchantStatus = merchantStatusRepository.findAll();
        log.info("End getAll");
        return merchantStatus.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private MerchantStatus findByStatusOrThrowNotFound(EMerchantStatus status) {
        return merchantStatusRepository.findByStatus(status)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "merchant status not found"));
    }

    private MerchantStatusResponse mapToResponse(MerchantStatus merchantStatus) {
        return MerchantStatusResponse.builder()
                .merchantStatusID(merchantStatus.getMerchantStatusID())
                .status(merchantStatus.getStatus().toString())
                .createdAt(merchantStatus.getCreatedAt())
                .updatedAt(merchantStatus.getUpdatedAt())
                .build();
    }
}
