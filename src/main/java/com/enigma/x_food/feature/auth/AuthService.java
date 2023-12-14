package com.enigma.x_food.feature.auth;

import com.enigma.x_food.feature.auth.dto.request.AuthRequest;
import com.enigma.x_food.feature.auth.dto.response.LoginResponse;

public interface AuthService {
    LoginResponse login(AuthRequest request);
}
