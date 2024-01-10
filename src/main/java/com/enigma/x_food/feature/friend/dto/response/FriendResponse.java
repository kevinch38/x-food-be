package com.enigma.x_food.feature.friend.dto.response;

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
    private String accountID1;
    private String accountID2;
    private String accountFirstName1;
    private String accountFirstName2;
    private String accountLastName1;
    private String accountLastName2;
    private byte[] imageAccount1;
    private byte[] imageAccount2;
}
