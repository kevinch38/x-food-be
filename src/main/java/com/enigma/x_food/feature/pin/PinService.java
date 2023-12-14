package com.enigma.x_food.feature.pin;

import com.enigma.x_food.feature.pin.dto.request.NewPinRequest;
import com.enigma.x_food.feature.pin.dto.request.SearchPinRequest;
import com.enigma.x_food.feature.pin.dto.response.PinResponse;
import org.springframework.data.domain.Page;

public interface PinService {
    Pin createNew(NewPinRequest request);
    PinResponse update(UpdatePinRequest request);
    PinResponse getById(String id);
}
