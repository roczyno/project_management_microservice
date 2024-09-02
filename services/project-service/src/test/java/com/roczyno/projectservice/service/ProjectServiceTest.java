package com.roczyno.projectservice.service;

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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
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
		String jwt= "shksghsuknsnkgsnsgnsgnksgsg";
		UserResponse mockedUserResponse=
				new UserResponse(userId,"jacob","jacob@gmail.com",5, LocalDateTime.now());
		ChatResponse mockedChatResponse= new ChatResponse(1,projectId, Set.of(1,2),LocalDateTime.now());
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
				.chatId(1)
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
				.chatId(1)
				.build();


		when(userService.getUserProfile(anyString())).thenReturn(mockedUserResponse);
		when(subscriptionService.getUserSubscription(eq(mockedUserResponse.id()))).thenReturn(mockedSubscriptionResponse);
		when(chatService.createChat(any(Chat.class),eq(projectId))).thenReturn(mockedChatResponse);
		when(projectRepository.save(any(Project.class))).thenReturn(mockProject);
		when(mapper.mapToProjectResponse(any(Project.class))).thenReturn(expectedResponse);

		ProjectResponse actualResponse= projectService.createProject(projectRequest,jwt);
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

}
