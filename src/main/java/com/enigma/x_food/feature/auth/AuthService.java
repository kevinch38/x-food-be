package com.enigma.x_food.feature.auth;

import com.enigma.x_food.feature.auth.dto.request.AdminAuthRequest;
import com.enigma.x_food.feature.auth.dto.request.AdminTokenRequest;
import com.enigma.x_food.feature.auth.dto.request.AuthRequest;
import com.enigma.x_food.feature.auth.dto.response.LoginResponse;

public interface AuthService {
    LoginResponse loginUser(AuthRequest request);
    LoginResponse loginAdmin(AdminAuthRequest request);
    Boolean verifyAdmin(AdminTokenRequest request);
}
