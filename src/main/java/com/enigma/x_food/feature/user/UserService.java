package com.enigma.x_food.feature.user;

import com.enigma.x_food.feature.user.dto.request.NewUserRequest;
import com.enigma.x_food.feature.user.dto.request.UpdateUserProfilePhotoRequest;
import com.enigma.x_food.feature.user.dto.request.UpdateUserRequest;
import com.enigma.x_food.feature.user.dto.response.UserResponse;
import com.enigma.x_food.feature.user.dto.request.SearchUserRequest;
import org.springframework.data.domain.Page;

public interface UserService {
    UserResponse createNew(NewUserRequest request);
    Page<UserResponse> getAll(SearchUserRequest request);
    UserResponse getById(String id);
    User getUserById(String id);
    UserResponse getUserByPhoneNumber(String phoneNumber);
    User getUserByPhoneNumber2(String phoneNumber);
    UserResponse update(UpdateUserRequest request);
    UserResponse updateProfilePhoto(UpdateUserProfilePhotoRequest request);
    void deleteById(String id);


}
