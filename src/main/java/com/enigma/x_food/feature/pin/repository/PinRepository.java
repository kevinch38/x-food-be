package com.enigma.x_food.feature.pin.repository;

import com.enigma.x_food.feature.pin.entity.Pin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PinRepository extends JpaRepository<Pin, String> {
}
