package com.enigma.x_food.feature.merchant_branch_status;

import com.enigma.x_food.constant.EMerchantBranchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MerchantBranchStatusRepository extends JpaRepository<MerchantBranchStatus, String> {
    Optional<MerchantBranchStatus> findByStatus(EMerchantBranchStatus status);
}
