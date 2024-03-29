package com.enigma.x_food.feature.user;

import com.enigma.x_food.feature.user.dto.request.*;
import com.enigma.x_food.feature.user.dto.response.UserResponse;
import com.enigma.x_food.shared.CommonResponse;
import com.enigma.x_food.feature.user.dto.request.NewUserRequest;
import com.enigma.x_food.shared.PagingResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<?> createNew(@RequestBody NewUserRequest request) {
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

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("permitAll")
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

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'PARTNERSHIP_STAFF', 'MARKETING_STAFF', 'PARTNERSHIP_HEAD', 'MARKETING_HEAD')")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(required = false, defaultValue = "accountID") String sortBy,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startCreatedAt,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endCreatedAt,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startUpdatedAt,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endUpdatedAt,
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
                .startUpdatedAt(startUpdatedAt)
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

    @GetMapping(value = "/{phoneNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getByPhoneNumber(@PathVariable String phoneNumber) {

        UserResponse userResponse = userService.getUserByPhoneNumber(phoneNumber);
        if (userResponse != null){
            CommonResponse<UserResponse> response = CommonResponse.<UserResponse>builder()
                    .message("successfully get user")
                    .statusCode(HttpStatus.OK.value())
                    .data(userResponse)
                    .build();
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(response);
        }
        CommonResponse<UserResponse> response = CommonResponse.<UserResponse>builder()
                .message("user not found")
                .statusCode(HttpStatus.OK.value())
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);

    }

    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "/ktp/{ktpID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getByKtpID(@PathVariable String ktpID) {

        UserResponse userResponse = userService.getUserByKtpID(ktpID);
        if (userResponse != null){
            CommonResponse<UserResponse> response = CommonResponse.<UserResponse>builder()
                    .message("successfully get user")
                    .statusCode(HttpStatus.OK.value())
                    .data(userResponse)
                    .build();
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(response);
        }
        CommonResponse<UserResponse> response = CommonResponse.<UserResponse>builder()
                .message("user not found")
                .statusCode(HttpStatus.OK.value())
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);

    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(
            @RequestBody UpdateUserRequest request
//            @RequestParam String accountID,
//            @RequestParam String ktpID,
//            @RequestParam String accountEmail,
//            @RequestParam String phoneNumber,
//            @RequestParam String firstName,
//            @RequestParam String lastName,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String dateOfBirth,
//            @RequestParam MultipartFile profilePhoto
            )
    {
//        UpdateUserRequest request = UpdateUserRequest.builder()
//                .accountID(accountID)
//                .ktpID(ktpID)
//                .accountEmail(accountEmail)
//                .phoneNumber(phoneNumber)
//                .firstName(firstName)
//                .lastName(lastName)
//                .dateOfBirth(LocalDate.parse(dateOfBirth, DateTimeFormatter.ISO_DATE_TIME))
//                .profilePhoto(profilePhoto)
//                .build();
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
}
