package com.enigma.x_food.feature.friend;

import com.enigma.x_food.feature.friend.dto.request.FriendRequest;
import com.enigma.x_food.feature.friend.dto.request.SearchFriendRequest;
import com.enigma.x_food.feature.friend.dto.response.FriendResponse;
import com.enigma.x_food.shared.CommonResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
@SecurityRequirement(name = "Bearer Authentication")
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
    public ResponseEntity<?> getAllOrGetByFriendID(
            @RequestParam String accountID,
            @RequestParam(required = false) String friendAccountID
    ) {
        SearchFriendRequest request = SearchFriendRequest.builder()
                .accountID(accountID)
                .friendAccountID(friendAccountID)
                .build();

        if (friendAccountID != null) {
            List<FriendResponse> friendResponses = friendService.findByFriendId(request);

            CommonResponse<List<FriendResponse>> response = CommonResponse.<List<FriendResponse>>builder()
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
