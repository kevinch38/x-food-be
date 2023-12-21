package com.enigma.x_food.feature.loyalty_point;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LoyaltyPointRepository extends JpaRepository<LoyaltyPoint, String>, JpaSpecificationExecutor<LoyaltyPoint> {
}
