package com.enigma.x_food.feature.user;

import com.enigma.x_food.feature.user.User;
import com.enigma.x_food.feature.user.dto.request.NewUserRequest;
import com.enigma.x_food.feature.user.dto.request.SearchUserRequest;
import com.enigma.x_food.feature.user.dto.response.UserResponse;
import com.enigma.x_food.feature.user.UserService;
import com.enigma.x_food.shared.CommonResponse;
import com.enigma.x_food.shared.ErrorController;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class UserControllerTest {
    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void createUser() throws Exception{
        NewUserRequest request = NewUserRequest.builder()
                .accountEmail("a")
                .phoneNumber("a")
                .pinID("abc")
                .firstName("a")
                .lastName("a")
                .build();
        UserResponse userResponse = UserResponse.builder()
                .accountEmail("a")
                .phoneNumber("a")
                .pinID("abc")
                .firstName("a")
                .lastName("a")
                .dateOfBirth(LocalDate.of(1970,1,1).toString())
                .balanceID("a")
                .loyaltyPointID("a")
                .otpID("")
                .build();

        Mockito.when(userService.createNew(request)).thenReturn(userResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(result -> {
                    CommonResponse<UserResponse> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {}
                    );
                    Assertions.assertEquals(201, response.getStatusCode());
                    Assertions.assertEquals("a",response.getData().getAccountEmail());
                });
    }

    @Test
    void getAllUser() throws Exception {
        SearchUserRequest searchUserRequest = SearchUserRequest.builder()
                .page(1)
                .size(10)
                .direction("asc")
                .sortBy("accountID")
                .build();
        Sort.Direction direction = Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(
                searchUserRequest.getPage(),
                searchUserRequest.getSize(),
                direction,
                searchUserRequest.getSortBy());
        UserResponse userResponse = UserResponse.builder()
                .accountID("1")
                .ktpID("2")
                .accountEmail("Email")
                .phoneNumber("3")
                .pinID("4")
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .firstName("A")
                .lastName("B")
                .dateOfBirth(LocalDate.of(2001,10,28).toString())
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .balanceID("5")
                .loyaltyPointID("6")
                .otpID("7")
                .build();
        List<UserResponse> userResponses = List.of(userResponse);
        Page<UserResponse> pageUserResponses = new PageImpl<>(userResponses, pageable, 0);

        Mockito.when(userService.getAll(Mockito.any())).thenReturn(pageUserResponses);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(result -> {
                    CommonResponse<List<UserResponse>> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {}
                    );
                    Assertions.assertEquals(200, response.getStatusCode());
                    Assertions.assertEquals("1",response.getData().get(0).getAccountID());
                });
    }

    @Test
    void getUserById() throws Exception {
        String id = "1";
        UserResponse userResponse = UserResponse.builder()
                .accountID("1")
                .ktpID("2")
                .accountEmail("Email")
                .phoneNumber("3")
                .pinID("4")
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .firstName("A")
                .lastName("B")
                .dateOfBirth(LocalDate.of(2001,10,28).toString())
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .balanceID("5")
                .loyaltyPointID("6")
                .otpID("7")
                .build();

        Mockito.when(userService.getById(Mockito.any())).thenReturn(userResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/"+id)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(result -> {
                    CommonResponse<UserResponse> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {}
                    );
                    Assertions.assertEquals(200, response.getStatusCode());
                    Assertions.assertEquals("1",response.getData().getAccountID());
                });
    }

}