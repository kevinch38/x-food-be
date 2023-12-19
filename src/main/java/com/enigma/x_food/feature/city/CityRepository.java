package com.enigma.x_food.feature.city;

import com.enigma.x_food.feature.city.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, String> {
}
