package com.enigma.x_food.feature.loyalty_point;

import com.enigma.x_food.feature.loyalty_point.dto.request.NewLoyaltyPointRequest;
import com.enigma.x_food.feature.loyalty_point.dto.request.UpdateLoyaltyPointRequest;
import com.enigma.x_food.feature.loyalty_point.dto.response.LoyaltyPointResponse;
import com.enigma.x_food.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoyaltyPointServiceImpl implements LoyaltyPointService {
    private final LoyaltyPointRepository loyaltyPointRepository;
    private final ValidationUtil validationUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoyaltyPoint createNew(NewLoyaltyPointRequest request) {
        try {
            log.info("Start createNew");
            validationUtil.validate(request);

            int currentYear = Year.now().getValue();
            ZonedDateTime endOfYear = ZonedDateTime.of(
                    currentYear, 12, 31, 23, 59, 59, 999,
                    ZoneId.of("GMT+7"));
            Instant expiredTime = endOfYear.toInstant();
            Timestamp expiredDate = Timestamp.from(expiredTime);

            LoyaltyPoint loyaltyPoint = LoyaltyPoint.builder()
                    .loyaltyPointAmount(request.getLoyaltyPointAmount())
                    .expiredDate(expiredDate)
                    .build();

            loyaltyPointRepository.save(loyaltyPoint);
            log.info("End createNew");
            return loyaltyPoint;
        } catch (DataIntegrityViolationException e) {
            log.error("Error createNew: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "loyalty point already exist");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoyaltyPointResponse update(UpdateLoyaltyPointRequest request) {
        validationUtil.validate(request);

        LoyaltyPoint loyaltyPoint = findByIdOrThrowException(request.getLoyaltyPointID());
        loyaltyPoint.setLoyaltyPointAmount(loyaltyPoint.getLoyaltyPointAmount() + request.getLoyaltyPointAmount());

        return mapToResponse(loyaltyPointRepository.saveAndFlush(loyaltyPoint));
    }

    @Override
    @Transactional(readOnly = true)
    public LoyaltyPoint findById(String id) {
        validationUtil.validate(id);
        return findByIdOrThrowException(id);
    }

    private LoyaltyPoint findByIdOrThrowException(String id) {
        return loyaltyPointRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Loyalty point not found"));
    }

    private LoyaltyPointResponse mapToResponse(LoyaltyPoint loyaltyPoint) {
        return LoyaltyPointResponse.builder()
                .loyaltyPointID(loyaltyPoint.getLoyaltyPointID())
                .accountID(loyaltyPoint.getUser().getAccountID())
                .loyaltyPointAmount(loyaltyPoint.getLoyaltyPointAmount())
                .expiredDate(loyaltyPoint.getExpiredDate())
                .createdAt(loyaltyPoint.getCreatedAt())
                .updatedAt(loyaltyPoint.getUpdatedAt())
                .build();
    }
}
