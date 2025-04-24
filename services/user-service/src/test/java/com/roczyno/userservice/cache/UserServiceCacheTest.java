package com.roczyno.userservice.cache;

import com.roczyno.userservice.model.User;
import com.roczyno.userservice.repository.UserRepository;
import com.roczyno.userservice.response.UserResponse;
import com.roczyno.userservice.service.UserService;
import com.roczyno.userservice.util.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceCacheTest {

    @Autowired
    private UserService userService;

    @Autowired
    private CacheManager cacheManager;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserMapper userMapper;

    private User testUser;
    private UserResponse testUserResponse;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
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

        // Mock authentication
        authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(testUser);
        when(authentication.getName()).thenReturn(testUser.getEmail());

        // Mock repository and mapper
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(testUser);
        when(userMapper.mapToUserResponse(testUser)).thenReturn(testUserResponse);
        when(userMapper.mapToUser(testUserResponse)).thenReturn(testUser);
    }

    @Test
    void findUserProfileByJwt_CachesResult() {
        // First call should hit the repository
        UserResponse result1 = userService.findUserProfileByJwt(authentication);
        assertNotNull(result1);
        assertEquals(testUserResponse.id(), result1.id());

        // Second call should use the cache
        UserResponse result2 = userService.findUserProfileByJwt(authentication);
        assertNotNull(result2);
        assertEquals(testUserResponse.id(), result2.id());

        // Verify repository was called only once
        verify(userRepository, times(1)).findByEmail(testUser.getEmail());
    }

    @Test
    void findUserById_CachesResult() {
        // First call should hit the repository
        UserResponse result1 = userService.findUserById(1);
        assertNotNull(result1);
        assertEquals(testUserResponse.id(), result1.id());

        // Second call should use the cache
        UserResponse result2 = userService.findUserById(1);
        assertNotNull(result2);
        assertEquals(testUserResponse.id(), result2.id());

        // Verify repository was called only once
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void findAllUsersByIds_CachesResult() {
        // Setup
        List<Integer> userIds = Arrays.asList(1, 2);
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
        
        List<User> users = Arrays.asList(testUser, user2);
        List<UserResponse> userResponses = Arrays.asList(testUserResponse, userResponse2);
        
        when(userRepository.findByIdIsIn(userIds)).thenReturn(users);
        when(userMapper.mapToUserResponse(user2)).thenReturn(userResponse2);

        // First call should hit the repository
        List<UserResponse> result1 = userService.findAllUsersByIds(userIds);
        assertNotNull(result1);
        assertEquals(2, result1.size());

        // Second call should use the cache
        List<UserResponse> result2 = userService.findAllUsersByIds(userIds);
        assertNotNull(result2);
        assertEquals(2, result2.size());

        // Verify repository was called only once
        verify(userRepository, times(1)).findByIdIsIn(userIds);
    }

    @Test
    void increaseUserProjectSize_EvictsCache() {
        // First call to findUserById should hit the repository
        UserResponse result1 = userService.findUserById(1);
        assertNotNull(result1);
        assertEquals(testUserResponse.id(), result1.id());

        // Mock increase user project size
        User updatedUser = User.builder()
                .id(1)
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .projectSize(3)
                .enabled(true)
                .accountLocked(false)
                .createdAt(LocalDateTime.now())
                .build();

        UserResponse updatedUserResponse = new UserResponse(
                updatedUser.getId(),
                updatedUser.getUsername(),
                updatedUser.getEmail(),
                updatedUser.getProjectSize(),
                updatedUser.getCreatedAt()
        );

        when(userRepository.save(testUser)).thenReturn(updatedUser);
        when(userMapper.mapToUserResponse(updatedUser)).thenReturn(updatedUserResponse);

        // Increase user project size should evict cache
        userService.increaseUserProjectSize(1);

        // Next call to findUserById should hit the repository again
        when(userRepository.findById(1)).thenReturn(Optional.of(updatedUser));
        UserResponse result2 = userService.findUserById(1);
        assertNotNull(result2);

        // Verify repository was called twice (once for initial get, once after cache eviction)
        verify(userRepository, times(2)).findById(1);
    }

    @Test
    void decreaseUserProjectSize_EvictsCache() {
        // First call to findUserById should hit the repository
        UserResponse result1 = userService.findUserById(1);
        assertNotNull(result1);
        assertEquals(testUserResponse.id(), result1.id());

        // Mock decrease user project size
        User updatedUser = User.builder()
                .id(1)
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .projectSize(0)
                .enabled(true)
                .accountLocked(false)
                .createdAt(LocalDateTime.now())
                .build();

        UserResponse updatedUserResponse = new UserResponse(
                updatedUser.getId(),
                updatedUser.getUsername(),
                updatedUser.getEmail(),
                updatedUser.getProjectSize(),
                updatedUser.getCreatedAt()
        );

        when(userRepository.save(testUser)).thenReturn(updatedUser);
        when(userMapper.mapToUserResponse(updatedUser)).thenReturn(updatedUserResponse);

        // Decrease user project size should evict cache
        userService.decreaseUserProjectSize(1);

        // Next call to findUserById should hit the repository again
        when(userRepository.findById(1)).thenReturn(Optional.of(updatedUser));
        UserResponse result2 = userService.findUserById(1);
        assertNotNull(result2);

        // Verify repository was called twice (once for initial get, once after cache eviction)
        verify(userRepository, times(2)).findById(1);
    }
}
