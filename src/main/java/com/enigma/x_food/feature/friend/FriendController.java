package com.enigma.x_food.feature.friend;

import com.enigma.x_food.feature.friend.dto.request.FriendRequest;
import com.enigma.x_food.feature.friend.dto.request.SearchFriendRequest;
import com.enigma.x_food.feature.friend.dto.response.FriendResponse;
import com.enigma.x_food.shared.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNew(@RequestBody FriendRequest request) {
        FriendResponse friendResponse = friendService.createNew(request);
        CommonResponse<FriendResponse> response = CommonResponse.<FriendResponse>builder()
                .message("successfully create friend")
                .statusCode(HttpStatus.CREATED.value())
                .data(friendResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam String accountID,
            @RequestParam(required = false) String friendID
    ) {
        SearchFriendRequest request = SearchFriendRequest.builder()
                .accountID(accountID)
                .friendID(friendID)
                .build();

        if (friendID != null) {
            List<Friend> friendResponses = friendService.findByFriendId(request);

            CommonResponse<List<Friend>> response = CommonResponse.<List<Friend>>builder()
                    .message("successfully get friend")
                    .statusCode(HttpStatus.OK.value())
                    .data(friendResponses)
                    .build();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(response);
        }
        else {
            List<FriendResponse> friendResponses = friendService.findByAccountId(request);

            CommonResponse<List<FriendResponse>> response = CommonResponse.<List<FriendResponse>>builder()
                    .message("successfully get all friends")
                    .statusCode(HttpStatus.OK.value())
                    .data(friendResponses)
                    .build();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(response);
        }
    }
}
