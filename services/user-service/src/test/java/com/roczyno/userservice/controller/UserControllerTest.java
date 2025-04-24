package com.roczyno.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roczyno.userservice.response.UserResponse;
import com.roczyno.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserResponse testUserResponse;
    private UserResponse testUserResponse2;

    @BeforeEach
    void setUp() {
        // Create test user responses
        testUserResponse = new UserResponse(
                1,
                "testuser",
                "test@example.com",
                2,
                LocalDateTime.now()
        );

        testUserResponse2 = new UserResponse(
                2,
                "testuser2",
                "test2@example.com",
                3,
                LocalDateTime.now()
        );
    }

    @Test
    @WithMockUser
    void profile_Success() throws Exception {
        // Arrange
        when(userService.findUserProfileByJwt(any(Authentication.class))).thenReturn(testUserResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/user/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testUserResponse.id())))
                .andExpect(jsonPath("$.username", is(testUserResponse.username())))
                .andExpect(jsonPath("$.email", is(testUserResponse.email())))
                .andExpect(jsonPath("$.projectSize", is(testUserResponse.projectSize())));
    }

    @Test
    @WithMockUser
    void getUserById_Success() throws Exception {
        // Arrange
        when(userService.findUserById(anyInt())).thenReturn(testUserResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/user/{userId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testUserResponse.id())))
                .andExpect(jsonPath("$.username", is(testUserResponse.username())))
                .andExpect(jsonPath("$.email", is(testUserResponse.email())))
                .andExpect(jsonPath("$.projectSize", is(testUserResponse.projectSize())));
    }

    @Test
    @WithMockUser
    void findAllUsersByIds_Success() throws Exception {
        // Arrange
        List<Integer> userIds = Arrays.asList(1, 2);
        List<UserResponse> userResponses = Arrays.asList(testUserResponse, testUserResponse2);
        
        when(userService.findAllUsersByIds(anyList())).thenReturn(userResponses);

        // Act & Assert
        mockMvc.perform(post("/api/v1/user/find-by-ids")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userIds)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(testUserResponse.id())))
                .andExpect(jsonPath("$[0].username", is(testUserResponse.username())))
                .andExpect(jsonPath("$[1].id", is(testUserResponse2.id())))
                .andExpect(jsonPath("$[1].username", is(testUserResponse2.username())));
    }

    @Test
    @WithMockUser
    void increaseUserProjectSize_Success() throws Exception {
        // Arrange
        when(userService.increaseUserProjectSize(anyInt())).thenReturn("successful");

        // Act & Assert
        mockMvc.perform(put("/api/v1/user/increase/project-size/{userId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("successful")));
    }

    @Test
    @WithMockUser
    void decreaseUserProjectSize_Success() throws Exception {
        // Arrange
        when(userService.decreaseUserProjectSize(anyInt())).thenReturn("successful");

        // Act & Assert
        mockMvc.perform(put("/api/v1/user/decrease/project-size/{userId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("successful")));
    }
}
