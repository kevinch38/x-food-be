package com.enigma.x_food.feature.user.service;

import com.enigma.x_food.feature.user.dto.request.SearchUserRequest;
import com.enigma.x_food.feature.user.dto.response.UserResponse;
import org.springframework.data.domain.Page;

public interface UserService {
    Page<UserResponse> getAll(SearchUserRequest request);
}
