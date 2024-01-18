package com.enigma.x_food.feature.promotion_status;

import com.enigma.x_food.constant.EPromotionStatus;
import com.enigma.x_food.feature.promotion_status.response.PromotionStatusResponse;
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
public class PromotionStatusServiceImpl implements PromotionStatusService {
    private final PromotionStatusRepository promotionStatusRepository;

    @Transactional(rollbackFor = Exception.class)
    @PostConstruct
    public void init() {
        List<PromotionStatus> promotionStatus = new ArrayList<>();
        for (EPromotionStatus value : EPromotionStatus.values()) {
            Optional<PromotionStatus> status = promotionStatusRepository.findByStatus(value);
            if (status.isPresent()) continue;

            promotionStatus.add(PromotionStatus.builder()
                    .status(value)
                    .build());
        }
        promotionStatusRepository.saveAllAndFlush(promotionStatus);
    }

    @Transactional(readOnly = true)
    @Override
    public PromotionStatus getByStatus(EPromotionStatus status) {
        return findByStatusOrThrowNotFound(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromotionStatusResponse> getAll() {
        log.info("Start getAll");

        List<PromotionStatus> promotionStatus = promotionStatusRepository.findAll();
        log.info("End getAll");
        return promotionStatus.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private PromotionStatus findByStatusOrThrowNotFound(EPromotionStatus status) {
        return promotionStatusRepository.findByStatus(status)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "promotion status not found"));
    }

    private PromotionStatusResponse mapToResponse(PromotionStatus promotionStatus) {
        return PromotionStatusResponse.builder()
                .promotionStatusID(promotionStatus.getPromotionStatusID())
                .status(promotionStatus.getStatus().toString())
                .createdAt(promotionStatus.getCreatedAt())
                .updatedAt(promotionStatus.getUpdatedAt())
                .build();
    }
}
