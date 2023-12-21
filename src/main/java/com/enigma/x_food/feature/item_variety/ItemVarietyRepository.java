package com.enigma.x_food.feature.item_variety;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemVarietyRepository extends JpaRepository<ItemVariety, String> {
}
