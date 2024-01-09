package com.enigma.x_food.feature.merchant_branch_update_request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MerchantBranchUpdateRequestRepository extends JpaRepository<MerchantBranchUpdateRequest, String> {
}
