package com.enigma.x_food.feature.pin;

import com.enigma.x_food.feature.pin.Pin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PinRepository extends JpaRepository<Pin, String> {
}
