package com.enigma.x_food.feature.friend;

import com.enigma.x_food.feature.friend.dto.request.FriendRequest;
import com.enigma.x_food.feature.friend.dto.request.SearchFriendRequest;
import com.enigma.x_food.feature.friend.dto.response.FriendResponse;

import java.util.List;

public interface FriendService {
    FriendResponse createNew(FriendRequest request);
    FriendResponse findById(String id);
    List<FriendResponse> findByAccountId(SearchFriendRequest request);
    List<FriendResponse> findByFriendId(SearchFriendRequest request);
    List<Friend> getByFriendId(SearchFriendRequest request);
}
