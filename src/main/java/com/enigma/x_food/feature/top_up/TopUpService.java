package com.enigma.x_food.feature.top_up;

import com.enigma.x_food.feature.top_up.dto.request.SearchTopUpRequest;
import com.enigma.x_food.feature.top_up.dto.request.TopUpRequest;
import com.enigma.x_food.feature.top_up.dto.response.TopUpResponse;

import java.util.List;

public interface TopUpService {
    TopUpResponse createNew(TopUpRequest request);
    List<TopUpResponse> findByAccountId(SearchTopUpRequest request);
}
