package com.roczyno.projectservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roczyno.projectservice.external.user.UserResponse;
import com.roczyno.projectservice.request.ProjectRequest;
import com.roczyno.projectservice.response.ProjectResponse;
import com.roczyno.projectservice.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProjectService projectService;

    private ProjectRequest projectRequest;
    private ProjectResponse projectResponse;
    private ProjectResponse projectResponse2;
    private UserResponse userResponse;
    private UserResponse userResponse2;
    private final String JWT = "Bearer test-jwt-token";

    @BeforeEach
    void setUp() {
        // Create test project request
        projectRequest = ProjectRequest.builder()
                .name("Test Project")
                .description("Test Description")
                .category("Development")
                .tags(Arrays.asList("Java", "Spring"))
                .build();

        // Create test project responses
        projectResponse = ProjectResponse.builder()
                .id(1)
                .name("Test Project")
                .description("Test Description")
                .category("Development")
                .tags(Arrays.asList("Java", "Spring"))
                .userId(1)
                .chatId(1)
                .build();

        projectResponse2 = ProjectResponse.builder()
                .id(2)
                .name("Another Project")
                .description("Another Description")
                .category("Design")
                .tags(Arrays.asList("UI", "UX"))
                .userId(2)
                .chatId(2)
                .build();

        // Create test user responses
        userResponse = new UserResponse(1, "testuser", "test@example.com", 2, LocalDateTime.now());
        userResponse2 = new UserResponse(2, "testuser2", "test2@example.com", 3, LocalDateTime.now());
    }

    @Test
    void addProject_Success() throws Exception {
        // Arrange
        when(projectService.createProject(any(ProjectRequest.class), anyString())).thenReturn(projectResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/project/create")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", JWT)
                .content(objectMapper.writeValueAsString(projectRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(projectResponse.id())))
                .andExpect(jsonPath("$.name", is(projectResponse.name())))
                .andExpect(jsonPath("$.description", is(projectResponse.description())))
                .andExpect(jsonPath("$.category", is(projectResponse.category())))
                .andExpect(jsonPath("$.tags", hasSize(2)))
                .andExpect(jsonPath("$.userId", is(projectResponse.userId())));
    }

    @Test
    void getProject_Success() throws Exception {
        // Arrange
        when(projectService.getProject(anyInt())).thenReturn(projectResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/project/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(projectResponse.id())))
                .andExpect(jsonPath("$.name", is(projectResponse.name())))
                .andExpect(jsonPath("$.description", is(projectResponse.description())));
    }

    @Test
    void getAllProjects_Success() throws Exception {
        // Arrange
        List<ProjectResponse> projects = Arrays.asList(projectResponse, projectResponse2);
        when(projectService.getProjectByTeam(anyString(), anyString(), anyString())).thenReturn(projects);

        // Act & Assert
        mockMvc.perform(get("/api/v1/project/all")
                .header("Authorization", JWT)
                .param("category", "Development")
                .param("tag", "Java"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(projectResponse.id())))
                .andExpect(jsonPath("$[0].name", is(projectResponse.name())))
                .andExpect(jsonPath("$[1].id", is(projectResponse2.id())))
                .andExpect(jsonPath("$[1].name", is(projectResponse2.name())));
    }

    @Test
    void updateProject_Success() throws Exception {
        // Arrange
        when(projectService.updateProject(anyInt(), any(ProjectRequest.class), anyString())).thenReturn(projectResponse);

        // Act & Assert
        mockMvc.perform(put("/api/v1/project/{projectId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", JWT)
                .content(objectMapper.writeValueAsString(projectRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(projectResponse.id())))
                .andExpect(jsonPath("$.name", is(projectResponse.name())))
                .andExpect(jsonPath("$.description", is(projectResponse.description())));
    }

    @Test
    void deleteProject_Success() throws Exception {
        // Arrange
        when(projectService.deleteProject(anyInt(), anyString())).thenReturn("Project deleted successfully");

        // Act & Assert
        mockMvc.perform(delete("/api/v1/project/{projectId}", 1)
                .header("Authorization", JWT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Project deleted successfully")));
    }

    @Test
    void addUserToProject_Success() throws Exception {
        // Arrange
        when(projectService.addUserToProject(anyInt(), anyString())).thenReturn("User added successfully");

        // Act & Assert
        mockMvc.perform(post("/api/v1/project/add/{projectId}", 1)
                .header("Authorization", JWT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("User added successfully")));
    }

    @Test
    void removeUserFromProject_Success() throws Exception {
        // Arrange
        when(projectService.removeUserFromProject(anyInt(), anyInt(), anyString())).thenReturn("User removed successfully");

        // Act & Assert
        mockMvc.perform(post("/api/v1/project/remove/{projectId}/user/{userId}", 1, 2)
                .header("Authorization", JWT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("User removed successfully")));
    }

    @Test
    void getProjectTeam_Success() throws Exception {
        // Arrange
        List<UserResponse> users = Arrays.asList(userResponse, userResponse2);
        when(projectService.findProjectTeamByProjectId(anyInt(), anyString())).thenReturn(users);

        // Act & Assert
        mockMvc.perform(get("/api/v1/project/team/{projectId}", 1)
                .header("Authorization", JWT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(userResponse.id())))
                .andExpect(jsonPath("$[0].username", is(userResponse.username())))
                .andExpect(jsonPath("$[1].id", is(userResponse2.id())))
                .andExpect(jsonPath("$[1].username", is(userResponse2.username())));
    }

    @Test
    void searchProject_Success() throws Exception {
        // Arrange
        List<ProjectResponse> projects = Arrays.asList(projectResponse);
        when(projectService.searchProject(anyString(), anyString())).thenReturn(projects);

        // Act & Assert
        mockMvc.perform(get("/api/v1/project/search")
                .param("tag", "Java")
                .header("Authorization", JWT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(projectResponse.id())))
                .andExpect(jsonPath("$[0].name", is(projectResponse.name())));
    }
}
