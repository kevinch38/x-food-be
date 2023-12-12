package com.enigma.x_food.feature.user;

import com.enigma.x_food.feature.user.dto.request.NewUserRequest;
import com.enigma.x_food.feature.user.dto.response.UserResponse;
import com.enigma.x_food.feature.user.dto.request.SearchUserRequest;
import com.enigma.x_food.feature.user.dto.response.UserResponse;
import org.springframework.data.domain.Page;

public interface UserService {
    UserResponse createNew(NewUserRequest request);
    Page<UserResponse> getAll(SearchUserRequest request);
    UserResponse getById(String id);
}
