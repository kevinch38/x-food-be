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

            LoyaltyPoint loyaltyPoint = LoyaltyPoint.builder()
                    .loyaltyPointAmount(request.getLoyaltyPointAmount())
                    .expiredDate(request.getExpiredAt())
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
        loyaltyPoint.setLoyaltyPointAmount(request.getLoyaltyPointAmount());
        loyaltyPoint.setExpiredDate(request.getExpiredAt());

        return mapToResponse(loyaltyPointRepository.saveAndFlush(loyaltyPoint));
    }

    @Override
    @Transactional(readOnly = true)
    public LoyaltyPointResponse findById(String id) {
        validationUtil.validate(id);
        return mapToResponse(findByIdOrThrowException(id));
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
