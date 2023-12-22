package com.enigma.x_food.feature.balance;

import com.enigma.x_food.feature.balance.dto.request.NewBalanceRequest;
import com.enigma.x_food.feature.balance.dto.request.UpdateBalanceRequest;
import com.enigma.x_food.feature.balance.dto.response.BalanceResponse;

public interface BalanceService {
    Balance createNew(NewBalanceRequest request);
    BalanceResponse update(UpdateBalanceRequest request);
    BalanceResponse findById(String id);
    Balance getById(String balanceID);
}
