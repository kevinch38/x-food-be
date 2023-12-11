package com.enigma.x_food.feature.pin.service;

import com.enigma.x_food.feature.pin.dto.request.NewPinRequest;
import com.enigma.x_food.feature.pin.dto.response.PinResponse;
import com.enigma.x_food.feature.pin.dto.request.SearchPinRequest;
import org.springframework.data.domain.Page;

public interface PinService {
    PinResponse createNew(NewPinRequest request);
    Page<PinResponse> getAll(SearchPinRequest request);
}
