package com.enigma.x_food.feature.voucher_status;

import com.enigma.x_food.constant.EVoucherStatus;
import com.enigma.x_food.feature.voucher_status.response.VoucherStatusResponse;
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
public class VoucherStatusServiceImpl implements VoucherStatusService {
    private final VoucherStatusRepository voucherStatusRepository;

    @Transactional(rollbackFor = Exception.class)
    @PostConstruct
    public void init() {
        List<VoucherStatus> voucherStatus = new ArrayList<>();
        for (EVoucherStatus value : EVoucherStatus.values()) {
            Optional<VoucherStatus> status = voucherStatusRepository.findByStatus(value);
            if (status.isPresent()) continue;

            voucherStatus.add(VoucherStatus.builder()
                    .status(value)
                    .build());
        }
        voucherStatusRepository.saveAllAndFlush(voucherStatus);
    }

    @Transactional(readOnly = true)
    @Override
    public VoucherStatus getByStatus(EVoucherStatus status) {
        return findByStatusOrThrowNotFound(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VoucherStatusResponse> getAll() {
        log.info("Start getAll");

        List<VoucherStatus> voucherStatus = voucherStatusRepository.findAll();
        log.info("End getAll");
        return voucherStatus.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private VoucherStatus findByStatusOrThrowNotFound(EVoucherStatus status) {
        return voucherStatusRepository.findByStatus(status)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "voucher status not found"));
    }

    private VoucherStatusResponse mapToResponse(VoucherStatus voucherStatus) {
        return VoucherStatusResponse.builder()
                .voucherStatusID(voucherStatus.getVoucherStatusID())
                .status(voucherStatus.getStatus().toString())
                .createdAt(voucherStatus.getCreatedAt())
                .updatedAt(voucherStatus.getUpdatedAt())
                .build();
    }
}
