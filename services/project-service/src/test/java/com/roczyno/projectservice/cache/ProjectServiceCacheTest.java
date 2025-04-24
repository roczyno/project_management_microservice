package com.roczyno.projectservice.cache;

import com.roczyno.projectservice.model.Project;
import com.roczyno.projectservice.repository.ProjectRepository;
import com.roczyno.projectservice.request.ProjectRequest;
import com.roczyno.projectservice.response.ProjectResponse;
import com.roczyno.projectservice.service.ProjectService;
import com.roczyno.projectservice.service.impl.ProjectServiceImpl;
import com.roczyno.projectservice.util.ProjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProjectServiceCacheTest {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private CacheManager cacheManager;

    @MockBean
    private ProjectRepository projectRepository;

    @MockBean
    private ProjectMapper projectMapper;

    private Project project;
    private ProjectResponse projectResponse;
    private ProjectRequest projectRequest;

    @BeforeEach
    void setUp() {
        // Create test project
        project = Project.builder()
                .id(1)
                .name("Test Project")
                .description("Test Description")
                .category("Development")
                .tags(Arrays.asList("Java", "Spring"))
                .userId(1)
                .teamMemberIds(new HashSet<>(Arrays.asList(1, 2, 3)))
                .chatId(1)
                .createdAt(LocalDateTime.now())
                .build();

        // Create test project response
        projectResponse = ProjectResponse.builder()
                .id(1)
                .name("Test Project")
                .description("Test Description")
                .category("Development")
                .tags(Arrays.asList("Java", "Spring"))
                .userId(1)
                .chatId(1)
                .build();

        // Create test project request
        projectRequest = ProjectRequest.builder()
                .name("Updated Project")
                .description("Updated Description")
                .category("Updated Category")
                .tags(Arrays.asList("Updated", "Tags"))
                .build();

        // Mock repository and mapper
        when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        when(projectMapper.mapToProjectResponse(project)).thenReturn(projectResponse);
        when(projectMapper.mapToProject(projectResponse)).thenReturn(project);
    }

    @Test
    void getProject_CachesResult() {
        // First call should hit the repository
        ProjectResponse result1 = projectService.getProject(1);
        assertNotNull(result1);
        assertEquals(projectResponse.id(), result1.id());

        // Second call should use the cache
        ProjectResponse result2 = projectService.getProject(1);
        assertNotNull(result2);
        assertEquals(projectResponse.id(), result2.id());

        // Verify repository was called only once
        verify(projectRepository, times(1)).findById(1);
    }

    @Test
    void updateProject_EvictsCache() {
        // First call to getProject should hit the repository
        ProjectResponse result1 = projectService.getProject(1);
        assertNotNull(result1);
        assertEquals(projectResponse.id(), result1.id());

        // Mock update project
        Project updatedProject = Project.builder()
                .id(1)
                .name("Updated Project")
                .description("Updated Description")
                .category("Updated Category")
                .tags(Arrays.asList("Updated", "Tags"))
                .userId(1)
                .teamMemberIds(new HashSet<>(Arrays.asList(1, 2, 3)))
                .chatId(1)
                .createdAt(LocalDateTime.now())
                .build();

        ProjectResponse updatedProjectResponse = ProjectResponse.builder()
                .id(1)
                .name("Updated Project")
                .description("Updated Description")
                .category("Updated Category")
                .tags(Arrays.asList("Updated", "Tags"))
                .userId(1)
                .chatId(1)
                .build();

        when(projectRepository.save(project)).thenReturn(updatedProject);
        when(projectMapper.mapToProjectResponse(updatedProject)).thenReturn(updatedProjectResponse);

        // Update project should evict cache
        projectService.updateProject(1, projectRequest, "test-jwt");

        // Next call to getProject should hit the repository again
        when(projectRepository.findById(1)).thenReturn(Optional.of(updatedProject));
        ProjectResponse result2 = projectService.getProject(1);
        assertNotNull(result2);

        // Verify repository was called twice (once for initial get, once after cache eviction)
        verify(projectRepository, times(2)).findById(1);
    }

    @Test
    void deleteProject_EvictsCache() {
        // First call to getProject should hit the repository
        ProjectResponse result1 = projectService.getProject(1);
        assertNotNull(result1);
        assertEquals(projectResponse.id(), result1.id());

        // Delete project should evict cache
        projectService.deleteProject(1, "test-jwt");

        // Next call to getProject should hit the repository again
        ProjectResponse result2 = projectService.getProject(1);
        assertNotNull(result2);

        // Verify repository was called twice (once for initial get, once after cache eviction)
        verify(projectRepository, times(2)).findById(1);
    }

    @Test
    void addUserToProject_EvictsCache() {
        // First call to getProject should hit the repository
        ProjectResponse result1 = projectService.getProject(1);
        assertNotNull(result1);
        assertEquals(projectResponse.id(), result1.id());

        // Mock add user to project
        Project updatedProject = Project.builder()
                .id(1)
                .name("Test Project")
                .description("Test Description")
                .category("Development")
                .tags(Arrays.asList("Java", "Spring"))
                .userId(1)
                .teamMemberIds(new HashSet<>(Arrays.asList(1, 2, 3, 4)))
                .chatId(1)
                .createdAt(LocalDateTime.now())
                .build();

        when(projectRepository.findById(1)).thenReturn(Optional.of(updatedProject));

        // Add user to project should evict cache
        projectService.addUserToProject(1, "test-jwt");

        // Next call to getProject should hit the repository again
        ProjectResponse result2 = projectService.getProject(1);
        assertNotNull(result2);

        // Verify repository was called twice (once for initial get, once after cache eviction)
        verify(projectRepository, times(2)).findById(1);
    }

    @Test
    void removeUserFromProject_EvictsCache() {
        // First call to getProject should hit the repository
        ProjectResponse result1 = projectService.getProject(1);
        assertNotNull(result1);
        assertEquals(projectResponse.id(), result1.id());

        // Mock remove user from project
        Project updatedProject = Project.builder()
                .id(1)
                .name("Test Project")
                .description("Test Description")
                .category("Development")
                .tags(Arrays.asList("Java", "Spring"))
                .userId(1)
                .teamMemberIds(new HashSet<>(Arrays.asList(1, 3)))
                .chatId(1)
                .createdAt(LocalDateTime.now())
                .build();

        when(projectRepository.findById(1)).thenReturn(Optional.of(updatedProject));

        // Remove user from project should evict cache
        projectService.removeUserFromProject(1, 2, "test-jwt");

        // Next call to getProject should hit the repository again
        ProjectResponse result2 = projectService.getProject(1);
        assertNotNull(result2);

        // Verify repository was called twice (once for initial get, once after cache eviction)
        verify(projectRepository, times(2)).findById(1);
    }
}
