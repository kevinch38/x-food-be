package com.enigma.x_food.feature.merchant_branch;

import com.enigma.x_food.feature.merchant_branch.MerchantBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantBranchRepository extends JpaRepository<MerchantBranch, String> {
}
