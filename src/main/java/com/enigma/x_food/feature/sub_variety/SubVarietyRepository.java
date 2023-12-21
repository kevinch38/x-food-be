package com.enigma.x_food.feature.sub_variety;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubVarietyRepository extends JpaRepository<SubVariety, String> {
}
