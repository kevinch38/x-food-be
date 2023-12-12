package com.enigma.x_food.feature.user;

import com.enigma.x_food.feature.user.dto.request.NewUserRequest;
import com.enigma.x_food.feature.user.dto.request.SearchUserRequest;
import com.enigma.x_food.feature.user.dto.response.UserResponse;
import com.enigma.x_food.feature.user.User;
import com.enigma.x_food.feature.user.UserRepository;
import com.enigma.x_food.feature.user.UserService;
import com.enigma.x_food.util.ValidationUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ValidationUtil validationUtil;

    @Test
    void createUserResponse() {
        NewUserRequest request = NewUserRequest.builder()
                .accountEmail("a")
                .phoneNumber("a")
                .pinID("abc")
                .firstName("a")
                .lastName("a")
                .build();
        User user = User.builder()
                .accountEmail("a")
                .phoneNumber("a")
                .pinID("abc")
                .firstName("a")
                .lastName("a")
                .dateOfBirth(LocalDate.of(1970,1,1))
                .balanceID("a")
                .loyaltyPointID("a")
                .otpID("")
                .build();
        Mockito.doNothing().when(validationUtil).validate(Mockito.isA(NewUserRequest.class));

        Mockito.when(userRepository.saveAndFlush(Mockito.isA(User.class))).thenReturn(user);

        UserResponse actual = userService.createNew(request);
        System.out.println(actual);

        assertEquals(user.getPinID(),actual.getPinID());
    }

    @Test
    void findUserAll() {
        SearchUserRequest searchUserRequest = SearchUserRequest.builder()
                .direction("asc")
                .page(1)
                .size(10)
                .sortBy("accountID")
                .build();

        User user = User.builder()
                .accountID("1")
                .ktpID("2")
                .accountEmail("Email")
                .phoneNumber("3")
                .pinID("4")
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .firstName("A")
                .lastName("B")
                .dateOfBirth(LocalDate.of(2001,10,28))
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .balanceID("5")
                .loyaltyPointID("6")
                .otpID("7")
                .build();
        Page<User> userPage = new PageImpl<>(List.of(user));
        Mockito.when(userRepository.findAll(Mockito.isA(Specification.class),Mockito.isA(Pageable.class))).thenReturn(userPage);

        Page<UserResponse> actual = userService.getAll(searchUserRequest);

        assertEquals(1,actual.getTotalElements());

    }
    @Test
    void findUserById() {
        String id = "1";
        User user = User.builder()
                .accountID("1")
                .ktpID("2")
                .accountEmail("Email")
                .phoneNumber("3")
                .pinID("4")
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .firstName("A")
                .lastName("B")
                .dateOfBirth(LocalDate.of(2001,10,28))
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .balanceID("5")
                .loyaltyPointID("6")
                .otpID("7")
                .build();
        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));

        UserResponse userById = userService.getById(id);

        assertEquals("A",userById.getFirstName());
        assertEquals("B",userById.getLastName());
    }
}