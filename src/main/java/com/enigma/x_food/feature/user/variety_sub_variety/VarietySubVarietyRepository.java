package com.enigma.x_food.feature.user.variety_sub_variety;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VarietySubVarietyRepository extends JpaRepository<VarietySubVariety, String> {
}
