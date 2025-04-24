package com.roczyno.projectservice.repository;

import com.roczyno.projectservice.model.Project;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    private Project project1;
    private Project project2;
    private Project project3;

    @BeforeEach
    void setUp() {
        // Create test projects
        project1 = Project.builder()
                .name("Test Project 1")
                .description("Description for test project 1")
                .category("Development")
                .tags(Arrays.asList("Java", "Spring"))
                .userId(1)
                .teamMemberIds(new HashSet<>(Arrays.asList(1, 2, 3)))
                .chatId(1)
                .createdAt(LocalDateTime.now())
                .build();

        project2 = Project.builder()
                .name("Test Project 2")
                .description("Description for test project 2")
                .category("Design")
                .tags(Arrays.asList("UI", "UX"))
                .userId(2)
                .teamMemberIds(new HashSet<>(Arrays.asList(2, 3, 4)))
                .chatId(2)
                .createdAt(LocalDateTime.now())
                .build();

        project3 = Project.builder()
                .name("Another Project")
                .description("Description for another project")
                .category("Testing")
                .tags(Arrays.asList("JUnit", "Mockito"))
                .userId(3)
                .teamMemberIds(new HashSet<>(Arrays.asList(1, 3, 5)))
                .chatId(3)
                .createdAt(LocalDateTime.now())
                .build();

        // Save projects to the repository
        projectRepository.saveAll(Arrays.asList(project1, project2, project3));
    }

    @AfterEach
    void tearDown() {
        projectRepository.deleteAll();
    }

    @Test
    void findByTeamOrOwner_WhenUserIsOwner_ReturnsProject() {
        // When
        List<Project> result = projectRepository.findByTeamOrOwner(1, 1);

        // Then
        assertEquals(1, result.size());
        assertTrue(result.contains(project1));
    }

    @Test
    void findByTeamOrOwner_WhenUserIsTeamMember_ReturnsProject() {
        // When
        List<Project> result = projectRepository.findByTeamOrOwner(4, 4);

        // Then
        assertEquals(1, result.size());
        assertTrue(result.contains(project2));
    }

    @Test
    void findByTeamOrOwner_WhenUserIsOwnerAndTeamMember_ReturnsProjects() {
        // When
        List<Project> result = projectRepository.findByTeamOrOwner(3, 3);

        // Then
        assertEquals(2, result.size());
        assertTrue(result.contains(project3)); // User is owner
        assertTrue(result.stream().anyMatch(p -> p.getUserId() != 3 && p.getTeamMemberIds().contains(3))); // User is team member
    }

    @Test
    void findTeamMemberIdsByProjectId_ReturnsTeamMemberIds() {
        // When
        List<Integer> result = projectRepository.findTeamMemberIdsByProjectId(project1.getId());

        // Then
        assertEquals(3, result.size());
        assertTrue(result.containsAll(Set.of(1, 2, 3)));
    }

    @Test
    void findByNameContainingIgnoreCase_ReturnsMatchingProjects() {
        // When
        List<Project> result = projectRepository.findByNameContainingIgnoreCase("test");

        // Then
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(p -> p.getName().toLowerCase().contains("test")));
    }

    @Test
    void findByNameContainingIgnoreCase_WithNoMatches_ReturnsEmptyList() {
        // When
        List<Project> result = projectRepository.findByNameContainingIgnoreCase("nonexistent");

        // Then
        assertTrue(result.isEmpty());
    }
}
