package com.enigma.x_food.feature.merchant_branch.repository;

import com.enigma.x_food.feature.merchant_branch.entity.MerchantBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantBranchRepository extends JpaRepository<MerchantBranch, String> {
}
