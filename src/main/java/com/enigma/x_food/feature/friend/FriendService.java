package com.enigma.x_food.feature.friend;

import com.enigma.x_food.feature.friend.dto.request.FriendRequest;
import com.enigma.x_food.feature.friend.dto.request.SearchFriendRequest;
import com.enigma.x_food.feature.friend.dto.response.FriendResponse;

import java.util.List;

public interface FriendService {
    FriendResponse createNew(FriendRequest request);
    List<FriendResponse> findByAccountId(SearchFriendRequest request);
    List<Friend> findByFriendId(SearchFriendRequest request);
}
