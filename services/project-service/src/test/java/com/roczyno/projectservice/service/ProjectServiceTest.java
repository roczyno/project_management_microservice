package com.roczyno.projectservice.service;

import com.roczyno.projectservice.exception.ProjectException;
import com.roczyno.projectservice.external.chat.Chat;
import com.roczyno.projectservice.external.chat.ChatResponse;
import com.roczyno.projectservice.external.chat.ChatService;
import com.roczyno.projectservice.external.subscription.PlanType;
import com.roczyno.projectservice.external.subscription.SubscriptionResponse;
import com.roczyno.projectservice.external.subscription.SubscriptionService;
import com.roczyno.projectservice.external.user.UserResponse;
import com.roczyno.projectservice.external.user.UserService;
import com.roczyno.projectservice.model.Project;
import com.roczyno.projectservice.repository.ProjectRepository;
import com.roczyno.projectservice.request.ProjectRequest;
import com.roczyno.projectservice.response.ProjectResponse;
import com.roczyno.projectservice.service.impl.ProjectServiceImpl;
import com.roczyno.projectservice.util.ProjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


class ProjectServiceTest {

	@Mock
	private  ProjectRepository projectRepository;
	@Mock
	private  ProjectMapper mapper;
	@Mock
	private  UserService userService;
	@Mock
	private  ChatService chatService;
	@Mock
	private  SubscriptionService subscriptionService;
	@InjectMocks
	private ProjectServiceImpl projectService;

	private AutoCloseable closeable;

	@BeforeEach
	void setUp() {
		closeable = MockitoAnnotations.openMocks(this);
	}

	@AfterEach
	void tearDown() throws Exception {
		closeable.close();
	}

	@Test
	void createProject_success(){
		Integer userId=1;
		Integer projectId=1;
		Integer chatId=1;
		UserResponse mockedUserResponse=
				new UserResponse(userId,"jacob","jacob@gmail.com",5, LocalDateTime.now());
		ChatResponse mockedChatResponse= new ChatResponse(chatId,projectId, Set.of(1,2),LocalDateTime.now());
		SubscriptionResponse mockedSubscriptionResponse=
				new SubscriptionResponse(1,LocalDateTime.now(),LocalDateTime.now().plusDays(2),
						true, PlanType.ANNUALLY,LocalDateTime.now());

		ProjectRequest projectRequest=ProjectRequest
				.builder()
				.name("project")
				.description("description")
				.category("category")
				.tags(List.of("frontend","backend"))
				.build();

		ProjectResponse expectedResponse=ProjectResponse
				.builder()
				.id(projectId)
				.name("project")
				.description("description")
				.category("category")
				.tags(List.of("frontend","backend"))
				.chatId(chatId)
				.userId(userId)
				.build();

		Project mockProject = Project
				.builder()
				.name("project")
				.description("description")
				.tags(List.of("frontend","backend"))
				.category("category")
				.createdAt(LocalDateTime.now())
				.userId(userId)
				.build();


		when(userService.getUserProfile(anyString())).thenReturn(mockedUserResponse);
		when(subscriptionService.getUserSubscription(eq(mockedUserResponse.id()))).thenReturn(mockedSubscriptionResponse);
		when(chatService.createChat(any(Chat.class),eq(projectId))).thenReturn(mockedChatResponse);
		when(projectRepository.save(any(Project.class))).thenReturn(mockProject);
		when(mapper.mapToProjectResponse(any(Project.class))).thenReturn(expectedResponse);

		ProjectResponse actualResponse= projectService.createProject(projectRequest,anyString());
		assertEquals(expectedResponse,actualResponse);
		assertNotNull(actualResponse);

	}

	@Test
	void getProject_success(){
		Integer projectId=1;
		Integer userId=1;
		Project mockProject = Project
				.builder()
				.name("project")
				.description("description")
				.tags(List.of("frontend","backend"))
				.category("category")
				.createdAt(LocalDateTime.now())
				.userId(userId)
				.chatId(1)
				.build();
		ProjectResponse expectedResponse=ProjectResponse
				.builder()
				.id(projectId)
				.name("project")
				.description("description")
				.category("category")
				.tags(List.of("frontend","backend"))
				.chatId(1)
				.userId(userId)
				.build();
		when(projectRepository.findById(projectId)).thenReturn(Optional.ofNullable(mockProject));
		when(mapper.mapToProjectResponse(any(Project.class))).thenReturn(expectedResponse);

		ProjectResponse actualResponse=projectService.getProject(projectId);
		assertNotNull(actualResponse);
		assertEquals(expectedResponse.name(),actualResponse.name());
		verify(projectRepository,times(1)).findById(projectId);
	}
	@Test
	void testGetProject_ThrowsProjectException_WhenProjectNotFound() {
		// Arrange
		when(projectRepository.findById(anyInt())).thenReturn(Optional.empty());

		// Act & Assert
		ProjectException exception = assertThrows(ProjectException.class, () -> projectService.getProject(1));
		assertEquals("Project not found", exception.getMessage());

		verify(projectRepository, times(1)).findById(anyInt());
	}
	@Test
	void testCreateProject_ThrowsProjectException_WhenMaxProjectsReached() {
		// Arrange
		ProjectRequest req =
				new ProjectRequest("Test Project", "Test Description","category" ,
						List.of("frontend","backend"));
		UserResponse userResponse =
				new UserResponse(1,"jacob","jacob@gmail.com",3, LocalDateTime.now());
		SubscriptionResponse mockedSubscriptionResponse=
				new SubscriptionResponse(1,LocalDateTime.now(),LocalDateTime.now().plusDays(2),
						true, PlanType.ANNUALLY,LocalDateTime.now());

		when(userService.getUserProfile(anyString())).thenReturn(userResponse);
		when(subscriptionService.getUserSubscription(anyInt())).thenReturn(mockedSubscriptionResponse);

		// Act & Assert
		ProjectException exception = assertThrows(ProjectException.class, () -> projectService.createProject(req, "test-jwt"));
		assertEquals("Users on a free plan can only create two projects", exception.getMessage());

		verify(userService, times(1)).getUserProfile(anyString());
		verify(subscriptionService, times(1)).getUserSubscription(anyInt());
	}

	@Test
	void testDeleteProject_Success() {
		Project project = Project
				.builder()
				.id(1)
				.name("project")
				.description("description")
				.tags(List.of("frontend","backend"))
				.category("category")
				.createdAt(LocalDateTime.now())
				.userId(1)
				.chatId(1)
				.build();
		UserResponse userResponse = new UserResponse(1,"jacob","jacob@gmail.com",3, LocalDateTime.now());

		when(mapper.mapToProject(any(ProjectResponse.class))).thenReturn(project);
		when(userService.getUserProfile(anyString())).thenReturn(userResponse);
		when(projectRepository.findById(eq(1))).thenReturn(Optional.of(project));

		String result = projectService.deleteProject(1, "test-jwt");

		assertEquals("Project deleted successfully", result);
		verify(projectRepository, times(1)).delete(any(Project.class));
	}

	@Test
	void testUpdateProject_Success() {
		ProjectRequest req = new ProjectRequest("Updated Project", "Updated Description",
				"Updated Category", List.of("updated tags"));
		Project project = new Project(1, "Test Project", "Test Description", "Test Category",
				List.of("Test Tag"), 1, Set.of(1), 1, LocalDateTime.now());
		UserResponse userResponse = new UserResponse(1,"jacob","jacob@gmail.com",3, LocalDateTime.now());

		when(mapper.mapToProject(any(ProjectResponse.class))).thenReturn(project);
		when(userService.getUserProfile(anyString())).thenReturn(userResponse);
		when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));
		when(projectRepository.save(any(Project.class))).thenReturn(project);
		when(mapper.mapToProjectResponse(any(Project.class))).thenReturn(new ProjectResponse(1,
				"Updated Project", "Updated Description",  "Updated Category",
				List.of("Updated Tag"), 1, 1));

		ProjectResponse result = projectService.updateProject(1, req, "test-jwt");

		assertNotNull(result);
		assertEquals("Updated Project", result.name());
		verify(projectRepository, times(1)).save(any(Project.class));
	}
	@Test
	void testAddUserToProject_Success() {
		Project project = new Project(1, "Test Project", "Test Description",
				"Test Category",List.of("Test Tag"), 1,Set.of(2), 1, LocalDateTime.now());
		UserResponse userResponse = new UserResponse(4, "New User", "newuser@example.com",
				0,LocalDateTime.now());

		when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));
		when(userService.getUserProfile(anyString())).thenReturn(userResponse);
		when(projectRepository.save(any(Project.class))).thenReturn(project);

		String result = projectService.addUserToProject(1, "test-jwt");

		assertEquals("User added successfully", result);
		verify(projectRepository, times(1)).save(any(Project.class));
		verify(chatService, times(1)).addUserToChat(anyInt(), anyInt());
	}
	@Test
	void testRemoveUserFromProject_Success() {
		Project project = new Project(1, "Test Project", "Test Description",  "Test Category",
				List.of("Test Tag"), 1, Set.of(2, 3), null, LocalDateTime.now());
		UserResponse userResponse = new UserResponse(1, "Test User", "test@example.com",
				1,LocalDateTime.now());
		UserResponse userToBeRemoved = new UserResponse(2, "User To Remove", "remove@example.com",
				0,LocalDateTime.now());

		when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));
		when(userService.getUserById(anyInt(), anyString())).thenReturn(userToBeRemoved);
		when(userService.getUserProfile(anyString())).thenReturn(userResponse);

		String result = projectService.removeUserFromProject(1, 2, "test-jwt");

		assertEquals("User removed successfully", result);
		verify(projectRepository, times(1)).save(any(Project.class));
		verify(chatService, times(1)).removeUserFromChat(anyInt(), anyInt());
	}
	@Test
	void testSearchProject_Success() {
		Project project1 = new Project(1, "Test Project 1", "Test Description 1",
				"Test Category",List.of("Test Tag"), 1, Set.of(1), 1, LocalDateTime.now());
		Project project2 = new Project(2, "Test Project 2", "Test Description 2",
				"Test Category",List.of("Test Tag"), 1, Set.of(1), 1, LocalDateTime.now());

		when(projectRepository.findByNameContainingIgnoreCase(anyString())).thenReturn(List.of(project1, project2));
		when(mapper.mapToProjectResponse(any(Project.class)))
				.thenReturn(new ProjectResponse(1, "Test Project 1", "Test Description 1",
						"Test Category",List.of("Test Tag"), 1, 1))
				.thenReturn(new ProjectResponse(2, "Test Project 2", "Test Description 2",
						"Test Category", List.of("Test Tag"),1, 1));

		List<ProjectResponse> result = projectService.searchProject("Test","test_jwt");

		assertNotNull(result);
		assertEquals(2, result.size());
		verify(projectRepository, times(1)).findByNameContainingIgnoreCase(anyString());
	}


}
