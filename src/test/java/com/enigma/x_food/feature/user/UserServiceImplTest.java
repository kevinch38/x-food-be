package com.enigma.x_food.feature.user;

import com.enigma.x_food.feature.otp.OTP;
import com.enigma.x_food.feature.pin.Pin;
import com.enigma.x_food.feature.user.dto.request.NewUserRequest;
import com.enigma.x_food.feature.user.dto.request.SearchUserRequest;
import com.enigma.x_food.feature.user.dto.request.UpdateUserRequest;
import com.enigma.x_food.feature.user.dto.response.UserResponse;
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
                .firstName("a")
                .lastName("a")
                .build();
        User user = User.builder()
                .accountEmail("a")
                .phoneNumber("a")
                .firstName("a")
                .lastName("a")
                .dateOfBirth(LocalDate.of(1970,1,1))
                .balanceID("a")
                .loyaltyPointID("a")
                .otp(OTP.builder().otpID("1").build())
                .build();
        Mockito.doNothing().when(validationUtil).validate(Mockito.isA(NewUserRequest.class));

        Mockito.when(userRepository.saveAndFlush(Mockito.isA(User.class))).thenReturn(user);

        UserResponse actual = userService.createNew(request);
        System.out.println(actual);

        assertEquals("a",actual.getAccountEmail());
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
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .firstName("A")
                .lastName("B")
                .dateOfBirth(LocalDate.of(2001,10,28))
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .balanceID("5")
                .loyaltyPointID("6")
                .otp(OTP.builder().otpID("1").build())
                .pin(Pin.builder().pinID("1").build())
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
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .firstName("A")
                .lastName("B")
                .dateOfBirth(LocalDate.of(2001,10,28))
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .balanceID("5")
                .loyaltyPointID("6")
                .otp(OTP.builder().otpID("1").build())
                .pin(Pin.builder().pinID("1").build())
                .build();
        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));

        UserResponse userById = userService.getById(id);

        assertEquals("A",userById.getFirstName());
        assertEquals("B",userById.getLastName());
    }
    @Test
    void getUserById(){
        String id = "1";
        User user = User.builder()
                .accountID("1")
                .ktpID("2")
                .accountEmail("Email")
                .phoneNumber("3")
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .firstName("A")
                .lastName("B")
                .dateOfBirth(LocalDate.of(2001,10,28))
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .balanceID("5")
                .loyaltyPointID("6")
                .otp(OTP.builder().otpID("1").build())
                .pin(Pin.builder().pinID("1").build())
                .build();
        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));

        User userById = userService.getUserById(id);

        assertEquals("A",userById.getFirstName());
        assertEquals("B",userById.getLastName());
    }
    @Test
    void getUserByPhoneNumber(){
        String phoneNumber = "3";
        User user = User.builder()
                .accountID("1")
                .ktpID("2")
                .accountEmail("Email")
                .phoneNumber("3")
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .firstName("A")
                .lastName("B")
                .dateOfBirth(LocalDate.of(2001,10,28))
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .balanceID("5")
                .loyaltyPointID("6")
                .otp(OTP.builder().otpID("1").build())
                .pin(Pin.builder().pinID("1").build())
                .build();
        Mockito.when(userRepository.findByPhoneNumber(Mockito.anyString())).thenReturn(Optional.of(user));

        UserResponse userById = userService.getUserByPhoneNumber(phoneNumber);

        assertEquals("A",userById.getFirstName());
        assertEquals("B",userById.getLastName());
    }
    @Test
    void getUserByPhoneNumber2(){
        String phoneNumber = "3";
        User user = User.builder()
                .accountID("1")
                .ktpID("2")
                .accountEmail("Email")
                .phoneNumber("3")
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .firstName("A")
                .lastName("B")
                .dateOfBirth(LocalDate.of(2001,10,28))
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .balanceID("5")
                .loyaltyPointID("6")
                .otp(OTP.builder().otpID("1").build())
                .pin(Pin.builder().pinID("1").build())
                .build();
        Mockito.when(userRepository.findByPhoneNumber(Mockito.anyString())).thenReturn(Optional.of(user));

        User userById = userService.getUserByPhoneNumber2(phoneNumber);

        assertEquals("A",userById.getFirstName());
        assertEquals("B",userById.getLastName());
    }
    @Test
    void update(){
        String id = "1";
        User user = User.builder()
                .accountID("1")
                .ktpID("2")
                .accountEmail("Email")
                .phoneNumber("3")
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .firstName("A")
                .lastName("B")
                .dateOfBirth(LocalDate.of(2001,10,28))
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .balanceID("5")
                .loyaltyPointID("6")
                .otp(OTP.builder().otpID("1").build())
                .pin(Pin.builder().pinID("1").build())
                .build();
        Mockito.doNothing().when(validationUtil).validate(Mockito.isA(NewUserRequest.class));
        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
        User user2 = User.builder()
                .accountID("1")
                .ktpID("2")
                .accountEmail("a")
                .phoneNumber("3")
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .firstName("A")
                .lastName("B")
                .dateOfBirth(LocalDate.of(2001,10,28))
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .balanceID("5")
                .loyaltyPointID("6")
                .otp(OTP.builder().otpID("1").build())
                .pin(Pin.builder().pinID("1").build())
                .build();
        Mockito.when(userRepository.saveAndFlush(Mockito.isA(User.class))).thenReturn(user2);

        UpdateUserRequest request = UpdateUserRequest.builder()
                .accountID(id)
                .ktpID("a")
                .accountEmail("a")
                .phoneNumber("a")
                .firstName("a")
                .lastName("a")
                .dateOfBirth(LocalDate.now())
                .build();
        UserResponse userById = userService.update(request);

        assertEquals("a",userById.getAccountEmail());
    }
    @Test
    void deleteById(){
        String id = "1";
        User user = User.builder()
                .accountID("1")
                .ktpID("2")
                .accountEmail("Email")
                .phoneNumber("3")
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .firstName("A")
                .lastName("B")
                .dateOfBirth(LocalDate.of(2001,10,28))
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .balanceID("5")
                .loyaltyPointID("6")
                .otp(OTP.builder().otpID("1").build())
                .pin(Pin.builder().pinID("1").build())
                .build();

        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));
        Mockito.doNothing().when(userRepository).delete(user);

        userService.deleteById(id);

        Mockito.verify(userRepository, Mockito.times(1)).findById(id);
        Mockito.verify(userRepository, Mockito.times(1)).delete(user);
    }
}