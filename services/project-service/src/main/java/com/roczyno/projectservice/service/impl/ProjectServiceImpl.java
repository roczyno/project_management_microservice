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
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectServiceImpl implements ProjectService {
	private static final String CHAT_BREAKER = "chatBreaker";
	private static final String USER_BREAKER = "userBreaker";
	private static final String SUBSCRIPTION_BREAKER = "subscriptionBreaker";

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
	@CircuitBreaker(name = CHAT_BREAKER, fallbackMethod = "createProjectFallback")
	@Retry(name = CHAT_BREAKER, fallbackMethod = "createProjectFallback")
	@RateLimiter(name = CHAT_BREAKER, fallbackMethod = "createProjectFallback")
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

	public ProjectResponse createProjectFallback(ProjectRequest req, String jwt, Exception e) {
		log.error("Circuit breaker triggered in createProject: {}", e.getMessage(), e);
		// Create a project without chat integration as fallback
		UserResponse user = userService.getUserProfile(jwt);
		Project newProject = createAndSaveNewProject(req, user.id());
		Project projectWithUser = addUserToProjectTeam(newProject, user.id());
		// Set a temporary note that chat creation failed
		newProject.setDescription((newProject.getDescription() != null ? newProject.getDescription() : "") 
			+ " [Note: Chat integration temporarily unavailable]");
		Project finalProject = projectRepository.save(projectWithUser);
		return mapper.mapToProjectResponse(finalProject);
	}

	@CircuitBreaker(name = SUBSCRIPTION_BREAKER, fallbackMethod = "validateUserProjectLimitFallback")
	@Retry(name = SUBSCRIPTION_BREAKER, fallbackMethod = "validateUserProjectLimitFallback")
	@RateLimiter(name = SUBSCRIPTION_BREAKER, fallbackMethod = "validateUserProjectLimitFallback")
	private void validateUserProjectLimit(UserResponse user) {
		SubscriptionResponse userSubscription = subscriptionService.getUserSubscription(user.id());
		if (userSubscription.planType() == PlanType.FREE && user.projectSize() > 2) {
			throw new ProjectException(ERROR_MAX_PROJECTS_REACHED_FREE);
		} else if (userSubscription.planType() == PlanType.MONTHLY && user.projectSize() > 10) {
			throw new ProjectException(ERROR_MAX_PROJECTS_REACHED_MONTHLY);
		}
	}

	private void validateUserProjectLimitFallback(UserResponse user, Exception e) {
		log.error("Circuit breaker triggered in validateUserProjectLimit: {}", e.getMessage(), e);
		// Fallback to a default behavior - allow the operation with a warning
		log.warn("Using fallback subscription validation for user {}", user.id());
		// We'll assume a FREE plan with conservative limits as a fallback
		if (user.projectSize() > 2) {
			throw new ProjectException("Subscription service unavailable. Using default limits: " + ERROR_MAX_PROJECTS_REACHED_FREE);
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
	@org.springframework.cache.annotation.Cacheable(value = "projects", key = "#projectId")
	public ProjectResponse getProject(Integer projectId) {
		Project project = projectRepository.findById(projectId)
				.orElseThrow(()-> new ProjectException(ERROR_PROJECT_NOT_FOUND));
		return mapper.mapToProjectResponse(project);
	}

	@CircuitBreaker(name = USER_BREAKER, fallbackMethod = "getProjectByTeamFallback")
	@Retry(name = USER_BREAKER, fallbackMethod = "getProjectByTeamFallback")
	@RateLimiter(name = USER_BREAKER, fallbackMethod = "getProjectByTeamFallback")
	@Transactional
	@org.springframework.cache.annotation.Cacheable(value = "userProjects", key = "{#jwt, #category, #tag}")
	public List<ProjectResponse> getProjectByTeam(String jwt, String category, String tag) {
		UserResponse user = userService.getUserProfile(jwt);
		List<Project> projects = projectRepository.findByTeamOrOwner(user.id(), user.id());

		projects = filterProjectsByCategoryAndTag(projects, category, tag);

		return projects.stream()
				.map(mapper::mapToProjectResponse)
				.toList();
	}

	public List<ProjectResponse> getProjectByTeamFallback(String jwt, String category, String tag, Exception e) {
		log.error("Circuit breaker triggered in getProjectByTeam: {}", e.getMessage(), e);
		// Return an empty list or cached data if available
		log.warn("Returning empty project list due to user service unavailability");
		return new ArrayList<>();
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
	@org.springframework.cache.annotation.CacheEvict(value = {"projects", "userProjects", "projectTeams", "projectSearch"}, allEntries = true)
	public String deleteProject(Integer projectId, String jwt) {
		Project project = validateOwnershipAndGetProject(projectId, jwt);
		projectRepository.delete(project);
		return "Project deleted successfully";
	}

	@Override
	@Transactional
	@org.springframework.cache.annotation.CacheEvict(value = {"projects", "userProjects", "projectSearch"}, key = "#projectId")
	@org.springframework.cache.annotation.CachePut(value = "projects", key = "#projectId")
	public ProjectResponse updateProject(Integer projectId, ProjectRequest req, String jwt) {
		Project project = validateOwnershipAndGetProject(projectId, jwt);
		updateProjectDetails(project, req);
		Project updatedProject = projectRepository.save(project);
		return mapper.mapToProjectResponse(updatedProject);
	}


	@Transactional
	@Override
	@CircuitBreaker(name = CHAT_BREAKER, fallbackMethod = "addUserToProjectFallback")
	@Retry(name = CHAT_BREAKER, fallbackMethod = "addUserToProjectFallback")
	@RateLimiter(name = CHAT_BREAKER, fallbackMethod = "addUserToProjectFallback")
	@org.springframework.cache.annotation.CacheEvict(value = {"projectTeams", "userProjects"}, allEntries = true)
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

	public String addUserToProjectFallback(Integer projectId, String jwt, Exception e) {
		log.error("Circuit breaker triggered in addUserToProject: {}", e.getMessage(), e);
		// Add user to project but not to chat
		Project project = projectRepository.findById(projectId)
				.orElseThrow(()-> new ProjectException("Project not found"));
		UserResponse user = userService.getUserProfile(jwt);

		if (project.getTeamMemberIds().contains(user.id()) || project.getUserId().equals(user.id())) {
			throw new ProjectException(ERROR_USER_ALREADY_PART_OF_TEAM);
		}

		project.getTeamMemberIds().add(user.id());
		projectRepository.save(project);
		return "User added to project successfully, but chat integration failed. Please try again later.";
	}

	@Override
	@Transactional
	@CircuitBreaker(name = CHAT_BREAKER, fallbackMethod = "removeUserFromProjectFallback")
	@Retry(name = CHAT_BREAKER, fallbackMethod = "removeUserFromProjectFallback")
	@RateLimiter(name = CHAT_BREAKER, fallbackMethod = "removeUserFromProjectFallback")
	@org.springframework.cache.annotation.CacheEvict(value = {"projectTeams", "userProjects"}, allEntries = true)
	public String removeUserFromProject(Integer projectId, Integer userId, String jwt) {
//		Project project = validateOwnershipAndGetProject(projectId, jwt);
		Project project=projectRepository.findById(projectId)
				.orElseThrow(()->new ProjectException("Project not found"));

		UserResponse userToBeRemoved=userService.getUserById(userId,jwt);
		UserResponse user = userService.getUserProfile(jwt);
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

	public String removeUserFromProjectFallback(Integer projectId, Integer userId, String jwt, Exception e) {
		log.error("Circuit breaker triggered in removeUserFromProject: {}", e.getMessage(), e);
		// Remove user from project but not from chat
		Project project = projectRepository.findById(projectId)
				.orElseThrow(()->new ProjectException("Project not found"));

		UserResponse userToBeRemoved = userService.getUserById(userId,jwt);
		UserResponse user = userService.getUserProfile(jwt);
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
		return "User removed from project successfully, but chat integration failed. Please try again later.";
	}

	@Override
	@org.springframework.cache.annotation.Cacheable(value = "projectSearch", key = "#keyword")
	public List<ProjectResponse> searchProject(String keyword, String jwt) {
		List<Project> projects=projectRepository.findByNameContainingIgnoreCase(keyword);
		return projects.stream()
				.map(mapper::mapToProjectResponse)
				.toList();
	}

	@CircuitBreaker(name = USER_BREAKER, fallbackMethod = "findProjectTeamByProjectIdFallback")
	@Retry(name = USER_BREAKER, fallbackMethod = "findProjectTeamByProjectIdFallback")
	@RateLimiter(name = USER_BREAKER, fallbackMethod = "findProjectTeamByProjectIdFallback")
	@Override
	@org.springframework.cache.annotation.Cacheable(value = "projectTeams", key = "#projectId")
	public List<UserResponse> findProjectTeamByProjectId(Integer projectId, String jwt) {
		List<Integer> teamIds = projectRepository.findTeamMemberIdsByProjectId(projectId);
		return userService.findAllUsersByIds(teamIds, jwt);
	}

	public List<UserResponse> findProjectTeamByProjectIdFallback(Integer projectId, String jwt, Exception e) {
		log.error("Circuit breaker triggered in findProjectTeamByProjectId: {}", e.getMessage(), e);
		// Return an empty list as fallback
		log.warn("Returning empty team list due to user service unavailability");
		return new ArrayList<>();
	}





	@CircuitBreaker(name = USER_BREAKER, fallbackMethod = "validateOwnershipAndGetProjectFallback")
	@Retry(name = USER_BREAKER, fallbackMethod = "validateOwnershipAndGetProjectFallback")
	@RateLimiter(name = USER_BREAKER, fallbackMethod = "validateOwnershipAndGetProjectFallback")
	private Project validateOwnershipAndGetProject(Integer projectId, String jwt) {
		Project project = mapper.mapToProject(getProject(projectId));
		UserResponse user = userService.getUserProfile(jwt);

		if (!project.getUserId().equals(user.id())) {
			throw new ProjectException(ERROR_ONLY_OWNER_CAN_MODIFY);
		}

		return project;
	}

	private Project validateOwnershipAndGetProjectFallback(Integer projectId, String jwt, Exception e) {
		log.error("Circuit breaker triggered in validateOwnershipAndGetProject: {}", e.getMessage(), e);
		// This is a critical operation that requires user validation
		// We can't provide a meaningful fallback that bypasses security
		throw new ProjectException("User service unavailable. Cannot validate project ownership at this time.");
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
