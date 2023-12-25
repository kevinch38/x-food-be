package com.enigma.x_food.feature.payment_status;

import com.enigma.x_food.constant.EPaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentStatusRepository extends JpaRepository<PaymentStatus, String> {
    Optional<PaymentStatus> findByStatus(EPaymentStatus status);
}
