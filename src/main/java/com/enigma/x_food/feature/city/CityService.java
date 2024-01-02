package com.enigma.x_food.feature.city;

import com.enigma.x_food.feature.city.dto.response.CityResponse;

import java.util.List;

public interface CityService {
    CityResponse getById(String id);
    List<CityResponse> getAll();
}
