package com.enigma.x_food.feature.user;

import com.enigma.x_food.feature.user.dto.request.*;
import com.enigma.x_food.feature.user.dto.response.UserResponse;
import com.enigma.x_food.shared.CommonResponse;
import com.enigma.x_food.feature.user.dto.request.NewUserRequest;
import com.enigma.x_food.shared.PagingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.enigma.x_food.util.PagingUtil;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping(path = "/register",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNewUser(@RequestBody NewUserRequest request) {
        UserResponse userResponse = userService.createNew(request);
        CommonResponse<UserResponse> response = CommonResponse.<UserResponse>builder()
                .message("successfully create new user")
                .statusCode(HttpStatus.CREATED.value())
                .data(userResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping(path = "/profile/photo",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProfilePhoto(
            @RequestParam String accountID,
            @RequestParam MultipartFile profilePhoto
    ) {
        UpdateUserProfilePhotoRequest request = UpdateUserProfilePhotoRequest.builder()
                .accountID(accountID)
                .profilePhoto(profilePhoto)
                .build();
        UserResponse userResponse = userService.updateProfilePhoto(request);
        CommonResponse<UserResponse> response = CommonResponse.<UserResponse>builder()
                .message("successfully update user profile photo")
                .statusCode(HttpStatus.OK.value())
                .data(userResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<?> getAllUser(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(required = false, defaultValue = "accountID") String sortBy,
            @RequestParam(required = false) LocalDate startCreatedAt,
            @RequestParam(required = false) LocalDate endCreatedAt,
            @RequestParam(required = false) LocalDate starUpdatedAt,
            @RequestParam(required = false) LocalDate endUpdatedAt,
            @RequestParam(required = false) String phoneNumber
    ) {
        page = PagingUtil.validatePage(page);
        size = PagingUtil.validateSize(size);
        direction = PagingUtil.validateDirection(direction);

        SearchUserRequest request = SearchUserRequest.builder()
                .page(page)
                .size(size)
                .direction(direction)
                .sortBy(sortBy)
                .phoneNumber(phoneNumber)
                .startCreatedAt(startCreatedAt)
                .endCreatedAt(endCreatedAt)
                .startUpdatedAt(starUpdatedAt)
                .endUpdatedAt(endUpdatedAt)
                .build();
        Page<UserResponse> users = userService.getAll(request);

       PagingResponse pagingResponse = PagingResponse.builder()
                .page(page)
                .size(size)
                .count(users.getTotalElements())
                .totalPages(users.getTotalPages())
                .build();

        CommonResponse<List<UserResponse>> response = CommonResponse.<List<UserResponse>>builder()
                .message("successfully get all user")
                .statusCode(HttpStatus.OK.value())
                .data(users.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

//    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> getUserById(@PathVariable String id) {
//        UserResponse userResponse = userService.getById(id);
//        CommonResponse<UserResponse> response = CommonResponse.<UserResponse>builder()
//                .message("successfully get user")
//                .statusCode(HttpStatus.OK.value())
//                .data(userResponse)
//                .build();
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(response);
//    }

    @GetMapping(value = "/{phoneNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserByPhoneNumber(@PathVariable String phoneNumber) {
        UserResponse userResponse = userService.getUserByPhoneNumber(phoneNumber);
        CommonResponse<UserResponse> response = CommonResponse.<UserResponse>builder()
                .message("successfully get user")
                .statusCode(HttpStatus.OK.value())
                .data(userResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }


    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserRequest request) {
        UserResponse userResponse = userService.update(request);
        CommonResponse<UserResponse> response = CommonResponse.<UserResponse>builder()
                .message("successfully update user")
                .statusCode(HttpStatus.OK.value())
                .data(userResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable String id) {
        userService.deleteById(id);
        CommonResponse<String> response = CommonResponse.<String>builder()
                .message("successfully delete user")
                .statusCode(HttpStatus.OK.value())
                .data("OK")
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

}
