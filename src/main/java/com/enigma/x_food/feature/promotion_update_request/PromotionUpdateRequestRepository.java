package com.enigma.x_food.feature.promotion_update_request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PromotionUpdateRequestRepository extends JpaRepository<PromotionUpdateRequest, String> {
}
