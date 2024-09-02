package com.roczyno.projectservice.service.impl;

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
import com.roczyno.projectservice.service.ProjectService;
import com.roczyno.projectservice.util.ProjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectServiceImpl implements ProjectService {
	private static final String CHAT_BREAKER = "chatBreaker";
	private static final String USER_BREAKER = "userBreaker";

	private static final String ERROR_PROJECT_NOT_FOUND = "Project not found";
	private static final String ERROR_ONLY_OWNER_CAN_MODIFY = "Only the owner of the project can modify it";
	private static final String ERROR_USER_ALREADY_PART_OF_TEAM = "User is already part of the team";
	private static final String ERROR_USER_NOT_PART_OF_TEAM = "User is not part of the team or already removed";
	private static final String ERROR_MAX_PROJECTS_REACHED_FREE = "Users on a free plan can only create two projects";
	private static final String ERROR_MAX_PROJECTS_REACHED_MONTHLY = "Users on a monthly plan can only create ten projects";

	private final ProjectRepository projectRepository;
	private final ProjectMapper mapper;
	private final UserService userService;
	private final ChatService chatService;
	private final SubscriptionService subscriptionService;

	@Transactional
	@Override
//	@CircuitBreaker(name = CHAT_BREAKER, fallbackMethod = "chatBreakerFallback")
//	@Retry(name = CHAT_BREAKER, fallbackMethod = "chatBreakerFallback")
//	@RateLimiter(name = CHAT_BREAKER, fallbackMethod = "chatBreakerFallback")
	public ProjectResponse createProject(ProjectRequest req, String jwt) {
		UserResponse user = userService.getUserProfile(jwt);
		validateUserProjectLimit(user);

		Project newProject = createAndSaveNewProject(req, user.id());
		Project projectWithUser = addUserToProjectTeam(newProject, user.id());

		ChatResponse projectChat = createProjectChat(projectWithUser);

		projectWithUser.setChatId(projectChat.id());
		Project finalProject = projectRepository.save(projectWithUser);

		userService.increaseUserProjectSize(user.id(),jwt);
		return mapper.mapToProjectResponse(finalProject);
	}

	private void validateUserProjectLimit(UserResponse user) {
		SubscriptionResponse userSubscription = subscriptionService.getUserSubscription(user.id());
		if (userSubscription.planType() == PlanType.FREE && user.projectSize() > 2) {
			throw new ProjectException(ERROR_MAX_PROJECTS_REACHED_FREE);
		} else if (userSubscription.planType() == PlanType.MONTHLY && user.projectSize() > 10) {
			throw new ProjectException(ERROR_MAX_PROJECTS_REACHED_MONTHLY);
		}
	}

	private Project createAndSaveNewProject(ProjectRequest req, Integer userId) {
		Project newProject = Project.builder()
				.name(req.name())
				.description(req.description())
				.tags(req.tags())
				.category(req.category())
				.createdAt(LocalDateTime.now())
				.userId(userId)
				.build();
		return projectRepository.save(newProject);
	}

	private Project addUserToProjectTeam(Project project, Integer userId) {
		project.getTeamMemberIds().add(userId);
		return projectRepository.save(project);
	}

	private ChatResponse createProjectChat(Project project) {
		Chat chat = new Chat();
		chat.setCreatedAt(LocalDateTime.now());
		chat.setProjectId(project.getId());
		return chatService.createChat(chat, project.getId());
	}



	@Override
	public ProjectResponse getProject(Integer projectId) {
		Project project = projectRepository.findById(projectId)
				.orElseThrow(()-> new ProjectException(ERROR_PROJECT_NOT_FOUND));
		return mapper.mapToProjectResponse(project);
	}

//	@CircuitBreaker(name = USER_BREAKER, fallbackMethod = "userBreakerFallback")
//	@Retry(name = USER_BREAKER, fallbackMethod = "userBreakerFallback")
//	@RateLimiter(name = USER_BREAKER, fallbackMethod = "userBreakerFallback")
	@Transactional
	public List<ProjectResponse> getProjectByTeam(String jwt, String category, String tag) {
		UserResponse user = userService.getUserProfile(jwt);
		List<Project> projects = projectRepository.findByTeamOrOwner(user.id(), user.id());

		projects = filterProjectsByCategoryAndTag(projects, category, tag);

		return projects.stream()
				.map(mapper::mapToProjectResponse)
				.toList();
	}

	private List<Project> filterProjectsByCategoryAndTag(List<Project> projects, String category, String tag) {
		if (category != null) {
			projects = projects.stream()
					.filter(project -> project.getCategory().equals(category))
					.toList();
		}
		if (tag != null) {
			projects = projects.stream()
					.filter(project -> project.getTags().contains(tag))
					.toList();
		}
		return projects;
	}

	@Override
	public String deleteProject(Integer projectId, String jwt) {
		Project project = validateOwnershipAndGetProject(projectId, jwt);
		projectRepository.delete(project);
		return "Project deleted successfully";
	}

	@Override
	@Transactional
	public ProjectResponse updateProject(Integer projectId, ProjectRequest req, String jwt) {
		Project project = validateOwnershipAndGetProject(projectId, jwt);
		updateProjectDetails(project, req);
		Project updatedProject = projectRepository.save(project);
		return mapper.mapToProjectResponse(updatedProject);
	}


	@Transactional
	@Override
//	@CircuitBreaker(name = CHAT_BREAKER, fallbackMethod = "chatBreakerFallback")
//	@Retry(name = CHAT_BREAKER, fallbackMethod = "chatBreakerFallback")
//	@RateLimiter(name = CHAT_BREAKER, fallbackMethod = "chatBreakerFallback")
	public String addUserToProject(Integer projectId, String jwt) {
		Project project =projectRepository.findById(projectId)
				.orElseThrow(()-> new ProjectException("Project not found"));
		UserResponse user = userService.getUserProfile(jwt);

		if (project.getTeamMemberIds().contains(user.id()) || project.getUserId().equals(user.id())) {
			throw new ProjectException(ERROR_USER_ALREADY_PART_OF_TEAM);
		}

		project.getTeamMemberIds().add(user.id());
		projectRepository.save(project);
		chatService.addUserToChat(projectId, user.id());
		return "User added successfully";
	}

	@Override
	@Transactional
//	@CircuitBreaker(name = CHAT_BREAKER, fallbackMethod = "chatBreakerFallback")
//	@Retry(name = CHAT_BREAKER, fallbackMethod = "chatBreakerFallback")
//	@RateLimiter(name = CHAT_BREAKER, fallbackMethod = "chatBreakerFallback")
	public String removeUserFromProject(Integer projectId, Integer userId, String jwt) {
//		Project project = validateOwnershipAndGetProject(projectId, jwt);
		Project project=projectRepository.findById(projectId)
				.orElseThrow(()->new ProjectException("Project not found"));

		UserResponse userToBeRemoved=userService.getUserById(userId,jwt);
		UserResponse user = userService.getUserProfile(jwt);
		log.info("this is the team {}",project.getTeamMemberIds());
		if(project.getUserId().equals(userToBeRemoved.id())){
			throw new ProjectException("the project owner can't be removed");
		}
		if(user.id().equals(userToBeRemoved.id())){
			throw new ProjectException("You cant remove yourself");
		}

		if (!project.getTeamMemberIds().contains(userToBeRemoved.id())) {
			throw new ProjectException(ERROR_USER_NOT_PART_OF_TEAM);
		}


		project.getTeamMemberIds().remove(userToBeRemoved.id());
		projectRepository.save(project);
		chatService.removeUserFromChat(projectId, userToBeRemoved.id());
		return "User removed successfully";
	}

	@Override
	public List<ProjectResponse> searchProject(String keyword, String jwt) {
		// Placeholder for actual implementation
		// This could include a repository query or service method for searching projects by keyword
		return List.of();
	}

//	@CircuitBreaker(name = USER_BREAKER, fallbackMethod = "userBreakerFallback")
//	@Retry(name = USER_BREAKER, fallbackMethod = "userBreakerFallback")
//	@RateLimiter(name = USER_BREAKER, fallbackMethod = "userBreakerFallback")
	@Override
	public List<UserResponse> findProjectTeamByProjectId(Integer projectId, String jwt) {
		List<Integer> teamIds = projectRepository.findTeamMemberIdsByProjectId(projectId);
		return userService.findAllUsersByIds(teamIds, jwt);
	}





	private Project validateOwnershipAndGetProject(Integer projectId, String jwt) {
		Project project = mapper.mapToProject(getProject(projectId));
		UserResponse user = userService.getUserProfile(jwt);

		if (!project.getUserId().equals(user.id())) {
			throw new ProjectException(ERROR_ONLY_OWNER_CAN_MODIFY);
		}

		return project;
	}






	private void updateProjectDetails(Project project, ProjectRequest req) {
		Optional.ofNullable(req.name()).ifPresent(project::setName);
		Optional.ofNullable(req.description()).ifPresent(project::setDescription);
		Optional.ofNullable(req.tags()).ifPresent(project::setTags);
		Optional.ofNullable(req.category()).ifPresent(project::setCategory);
	}

	// Fallback methods
//	public List<String> userBreakerFallback(Exception e) {
//		log.error("User service failed: {}", e.getMessage(), e);
//		List<String> list = new ArrayList<>();
//		list.add("User Service not available");
//		return list;
//	}
//	public List<String> chatBreakerFallback(Exception e) {
//		log.error("Chat service failed: {}", e.getMessage(), e);
//		List<String> list = new ArrayList<>();
//		list.add("Chat Service not available");
//		return list;
//	}


}
