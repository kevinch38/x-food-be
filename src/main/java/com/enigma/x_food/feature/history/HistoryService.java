package com.enigma.x_food.feature.history;

import com.enigma.x_food.feature.history.dto.request.HistoryRequest;
import com.enigma.x_food.feature.history.dto.request.SearchAccountHistoryRequest;
import com.enigma.x_food.feature.history.dto.request.SearchHistoryRequest;
import com.enigma.x_food.feature.history.dto.response.HistoryResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface HistoryService {
    History createNew(HistoryRequest request);
    List<HistoryResponse> findByAccountId(SearchAccountHistoryRequest request);
    History findById(String id);
    Page<HistoryResponse> findAll(SearchHistoryRequest request);
}
