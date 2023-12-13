package com.enigma.x_food.feature.pin;

import com.enigma.x_food.feature.pin.dto.request.NewPinRequest;
import com.enigma.x_food.feature.pin.dto.request.UpdatePinRequest;
import com.enigma.x_food.feature.pin.dto.response.PinResponse;

public interface PinService {
    Pin createNew(NewPinRequest request);
    PinResponse update(UpdatePinRequest request);
    PinResponse getById(String id);
}
