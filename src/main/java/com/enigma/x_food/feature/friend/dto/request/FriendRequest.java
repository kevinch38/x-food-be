package com.enigma.x_food.feature.friend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendRequest {
    @NotBlank(message = "Account ID 1 cannot be empty")
    private String accountID1;
    @NotBlank(message = "Account ID 2 cannot be empty")
    private String accountID2;
}
