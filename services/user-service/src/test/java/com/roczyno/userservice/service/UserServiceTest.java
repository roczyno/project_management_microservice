package com.roczyno.userservice.service;

import com.roczyno.userservice.exception.UserException;
import com.roczyno.userservice.model.User;
import com.roczyno.userservice.repository.UserRepository;
import com.roczyno.userservice.response.UserResponse;
import com.roczyno.userservice.service.impl.UserServiceImpl;
import com.roczyno.userservice.util.UserMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserServiceImpl userService;

    private AutoCloseable closeable;
    private User testUser;
    private UserResponse testUserResponse;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        // Create test user
        testUser = User.builder()
                .id(1)
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .projectSize(2)
                .enabled(true)
                .accountLocked(false)
                .createdAt(LocalDateTime.now())
                .build();

        // Create test user response
        testUserResponse = new UserResponse(
                testUser.getId(),
                testUser.getUsername(),
                testUser.getEmail(),
                testUser.getProjectSize(),
                testUser.getCreatedAt()
        );
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void findUserProfileByJwt_Success() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(testUser);
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(testUser);
        when(userMapper.mapToUserResponse(testUser)).thenReturn(testUserResponse);

        // Act
        UserResponse result = userService.findUserProfileByJwt(authentication);

        // Assert
        assertNotNull(result);
        assertEquals(testUserResponse.id(), result.id());
        assertEquals(testUserResponse.username(), result.username());
        assertEquals(testUserResponse.email(), result.email());
        
        // Verify repository was called
        verify(userRepository, times(1)).findByEmail(testUser.getEmail());
    }

    @Test
    void findUserById_Success() {
        // Arrange
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(userMapper.mapToUserResponse(testUser)).thenReturn(testUserResponse);

        // Act
        UserResponse result = userService.findUserById(testUser.getId());

        // Assert
        assertNotNull(result);
        assertEquals(testUserResponse.id(), result.id());
        assertEquals(testUserResponse.username(), result.username());
        assertEquals(testUserResponse.email(), result.email());
        
        // Verify repository was called
        verify(userRepository, times(1)).findById(testUser.getId());
    }

    @Test
    void findUserById_UserNotFound() {
        // Arrange
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        UserException exception = assertThrows(UserException.class, () -> userService.findUserById(999));
        assertEquals("User not found", exception.getMessage());
        
        // Verify repository was called
        verify(userRepository, times(1)).findById(anyInt());
    }

    @Test
    void findAllUsersByIds_Success() {
        // Arrange
        User user2 = User.builder()
                .id(2)
                .username("testuser2")
                .email("test2@example.com")
                .projectSize(3)
                .createdAt(LocalDateTime.now())
                .build();
        
        UserResponse userResponse2 = new UserResponse(
                user2.getId(),
                user2.getUsername(),
                user2.getEmail(),
                user2.getProjectSize(),
                user2.getCreatedAt()
        );
        
        List<Integer> userIds = Arrays.asList(1, 2);
        List<User> users = Arrays.asList(testUser, user2);
        
        when(userRepository.findByIdIsIn(userIds)).thenReturn(users);
        when(userMapper.mapToUserResponse(testUser)).thenReturn(testUserResponse);
        when(userMapper.mapToUserResponse(user2)).thenReturn(userResponse2);

        // Act
        List<UserResponse> result = userService.findAllUsersByIds(userIds);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testUserResponse.id(), result.get(0).id());
        assertEquals(userResponse2.id(), result.get(1).id());
        
        // Verify repository was called
        verify(userRepository, times(1)).findByIdIsIn(userIds);
    }

    @Test
    void increaseUserProjectSize_Success() {
        // Arrange
        User updatedUser = User.builder()
                .id(testUser.getId())
                .username(testUser.getUsername())
                .email(testUser.getEmail())
                .projectSize(testUser.getProjectSize() + 1)
                .createdAt(testUser.getCreatedAt())
                .build();
        
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(userMapper.mapToUserResponse(testUser)).thenReturn(testUserResponse);
        when(userMapper.mapToUser(testUserResponse)).thenReturn(testUser);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // Act
        String result = userService.increaseUserProjectSize(testUser.getId());

        // Assert
        assertEquals("successful", result);
        
        // Verify repository was called
        verify(userRepository, times(1)).findById(testUser.getId());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void decreaseUserProjectSize_Success() {
        // Arrange
        User updatedUser = User.builder()
                .id(testUser.getId())
                .username(testUser.getUsername())
                .email(testUser.getEmail())
                .projectSize(0)
                .createdAt(testUser.getCreatedAt())
                .build();
        
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(userMapper.mapToUserResponse(testUser)).thenReturn(testUserResponse);
        when(userMapper.mapToUser(testUserResponse)).thenReturn(testUser);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // Act
        String result = userService.decreaseUserProjectSize(testUser.getId());

        // Assert
        assertEquals("successful", result);
        
        // Verify repository was called
        verify(userRepository, times(1)).findById(testUser.getId());
        verify(userRepository, times(1)).save(any(User.class));
    }
}
