package com.roczyno.userservice.repository;

import com.roczyno.userservice.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        // Create test users
        user1 = User.builder()
                .username("testuser1")
                .email("test1@example.com")
                .password("password1")
                .projectSize(2)
                .enabled(true)
                .accountLocked(false)
                .createdAt(LocalDateTime.now())
                .build();

        user2 = User.builder()
                .username("testuser2")
                .email("test2@example.com")
                .password("password2")
                .projectSize(3)
                .enabled(true)
                .accountLocked(false)
                .createdAt(LocalDateTime.now())
                .build();

        // Save users to the repository
        userRepository.saveAll(Arrays.asList(user1, user2));
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void findByEmail_Success() {
        // When
        User result = userRepository.findByEmail(user1.getEmail());

        // Then
        assertNotNull(result);
        assertEquals(user1.getUsername(), result.getUsername());
        assertEquals(user1.getEmail(), result.getEmail());
        assertEquals(user1.getProjectSize(), result.getProjectSize());
    }

    @Test
    void findByEmail_NotFound() {
        // When
        User result = userRepository.findByEmail("nonexistent@example.com");

        // Then
        assertNull(result);
    }

    @Test
    void findByUsername_Success() {
        // When
        User result = userRepository.findByUsername(user2.getUsername());

        // Then
        assertNotNull(result);
        assertEquals(user2.getUsername(), result.getUsername());
        assertEquals(user2.getEmail(), result.getEmail());
        assertEquals(user2.getProjectSize(), result.getProjectSize());
    }

    @Test
    void findByUsername_NotFound() {
        // When
        User result = userRepository.findByUsername("nonexistentuser");

        // Then
        assertNull(result);
    }

    @Test
    void findByIdIsIn_Success() {
        // When
        List<User> result = userRepository.findByIdIsIn(Arrays.asList(user1.getId(), user2.getId()));

        // Then
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(u -> u.getEmail().equals(user1.getEmail())));
        assertTrue(result.stream().anyMatch(u -> u.getEmail().equals(user2.getEmail())));
    }

    @Test
    void findByIdIsIn_PartialMatch() {
        // When
        List<User> result = userRepository.findByIdIsIn(Arrays.asList(user1.getId(), 999));

        // Then
        assertEquals(1, result.size());
        assertEquals(user1.getEmail(), result.get(0).getEmail());
    }

    @Test
    void findByIdIsIn_NoMatch() {
        // When
        List<User> result = userRepository.findByIdIsIn(Arrays.asList(999, 1000));

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void findById_Success() {
        // When
        Optional<User> result = userRepository.findById(user1.getId());

        // Then
        assertTrue(result.isPresent());
        assertEquals(user1.getEmail(), result.get().getEmail());
    }

    @Test
    void findById_NotFound() {
        // When
        Optional<User> result = userRepository.findById(999);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void save_Success() {
        // Given
        User newUser = User.builder()
                .username("newuser")
                .email("new@example.com")
                .password("newpassword")
                .projectSize(1)
                .enabled(true)
                .accountLocked(false)
                .createdAt(LocalDateTime.now())
                .build();

        // When
        User savedUser = userRepository.save(newUser);

        // Then
        assertNotNull(savedUser.getId());
        assertEquals(newUser.getUsername(), savedUser.getUsername());
        assertEquals(newUser.getEmail(), savedUser.getEmail());
        assertEquals(newUser.getProjectSize(), savedUser.getProjectSize());

        // Verify it can be retrieved
        User retrievedUser = userRepository.findByEmail(newUser.getEmail());
        assertNotNull(retrievedUser);
        assertEquals(newUser.getUsername(), retrievedUser.getUsername());
    }

    @Test
    void update_Success() {
        // Given
        user1.setProjectSize(user1.getProjectSize() + 1);

        // When
        User updatedUser = userRepository.save(user1);

        // Then
        assertEquals(user1.getId(), updatedUser.getId());
        assertEquals(user1.getProjectSize(), updatedUser.getProjectSize());

        // Verify it was updated in the database
        User retrievedUser = userRepository.findById(user1.getId()).orElse(null);
        assertNotNull(retrievedUser);
        assertEquals(user1.getProjectSize(), retrievedUser.getProjectSize());
    }
}
