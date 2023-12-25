package com.enigma.x_food.feature.payment_status;

import com.enigma.x_food.constant.EPaymentStatus;
import com.enigma.x_food.feature.payment_status.response.PaymentStatusResponse;
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
public class PaymentStatusServiceImpl implements PaymentStatusService {
    private final PaymentStatusRepository paymentStatusRepository;

    @Transactional(rollbackFor = Exception.class)
    @PostConstruct
    private void init() {
        List<PaymentStatus> paymentStatus = new ArrayList<>();
        for (EPaymentStatus value : EPaymentStatus.values()) {
            Optional<PaymentStatus> status = paymentStatusRepository.findByStatus(value);
            if (status.isPresent()) continue;

            paymentStatus.add(PaymentStatus.builder()
                    .status(value)
                    .build());
        }
        paymentStatusRepository.saveAllAndFlush(paymentStatus);
    }

    @Transactional(readOnly = true)
    @Override
    public PaymentStatus getByStatus(EPaymentStatus status) {
        return findByStatusOrThrowNotFound(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentStatusResponse> getAll() {
        log.info("Start getAll");

        List<PaymentStatus> paymentStatus = paymentStatusRepository.findAll();
        log.info("End getAll");
        return paymentStatus.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private PaymentStatus findByStatusOrThrowNotFound(EPaymentStatus status) {
        return paymentStatusRepository.findByStatus(status)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "payment status not found"));
    }

    private PaymentStatusResponse mapToResponse(PaymentStatus paymentStatus) {
        return PaymentStatusResponse.builder()
                .paymentStatusID(paymentStatus.getPaymentStatusID())
                .status(paymentStatus.getStatus().toString())
                .createdAt(paymentStatus.getCreatedAt())
                .updatedAt(paymentStatus.getUpdatedAt())
                .build();
    }
}
