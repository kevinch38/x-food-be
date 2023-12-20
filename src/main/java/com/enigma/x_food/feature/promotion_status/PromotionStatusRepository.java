package com.enigma.x_food.feature.promotion_status;

import com.enigma.x_food.constant.EPromotionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PromotionStatusRepository extends JpaRepository<PromotionStatus, String> {
    Optional<PromotionStatus> findByStatus(EPromotionStatus status);
}
