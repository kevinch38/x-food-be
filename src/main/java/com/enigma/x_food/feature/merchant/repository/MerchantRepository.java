package com.enigma.x_food.feature.merchant.repository;

import com.enigma.x_food.feature.merchant.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, String> {
}
