package com.enigma.x_food.feature.user;

import com.enigma.x_food.feature.user.dto.request.NewUserRequest;
import com.enigma.x_food.feature.user.dto.response.UserResponse;
import com.enigma.x_food.feature.user.dto.request.SearchUserRequest;
import com.enigma.x_food.feature.user.dto.response.UserResponse;
import com.enigma.x_food.shared.CommonResponse;
import com.enigma.x_food.shared.ErrorController;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.enigma.x_food.util.PagingUtil;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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

    @GetMapping
    public ResponseEntity<?> getAllUser(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(required = false, defaultValue = "accountID") String sortBy,
            @RequestParam(required = false) String accountID,
            @RequestParam(required = false) String accountEmail,
            @RequestParam(required = false) String ktpID,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) LocalDate dateOfBirth
    ) {
        page = PagingUtil.validatePage(page);
        size = PagingUtil.validateSize(size);
        direction = PagingUtil.validateDirection(direction);

        SearchUserRequest request = SearchUserRequest.builder()
                .page(page)
                .size(size)
                .direction(direction)
                .sortBy(sortBy)
                .accountID(accountID)
                .accountEmail(accountEmail)
                .ktpID(ktpID)
                .phoneNumber(phoneNumber)
                .firstName(firstName)
                .lastName(lastName)
                .dateOfBirth(dateOfBirth)
                .build();
        Page<UserResponse> users = userService.getAll(request);

        ErrorController.PagingResponse pagingResponse = ErrorController.PagingResponse.builder()
                .page(page)
                .size(size)
                .count(users.getTotalElements())
                .totalPages(users.getTotalPages())
                .build();

        ErrorController.CommonResponse<List<UserResponse>> response = ErrorController.CommonResponse.<List<UserResponse>>builder()
                .message("successfully get all user")
                .statusCode(HttpStatus.OK.value())
                .data(users.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

}
