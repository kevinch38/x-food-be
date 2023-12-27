package com.enigma.x_food.feature.voucher_status;

import com.enigma.x_food.constant.EVoucherStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoucherStatusRepository extends JpaRepository<VoucherStatus, String> {
    Optional<VoucherStatus> findByStatus(EVoucherStatus status);
}
