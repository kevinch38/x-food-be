package com.enigma.x_food.feature.merchant_status;

import com.enigma.x_food.constant.EMerchantStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MerchantStatusRepository extends JpaRepository<MerchantStatus, String> {
    Optional<MerchantStatus> findByStatus(EMerchantStatus status);
}
