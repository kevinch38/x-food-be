package com.enigma.x_food.feature.friend.dto.response;

import com.enigma.x_food.feature.user.dto.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendResponse {
    private String friendID;
    private UserResponse user1;
    private UserResponse user2;
}
