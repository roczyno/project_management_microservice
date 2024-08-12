package com.roczyno.projectservice.service.impl;

import com.roczyno.projectservice.exception.ProjectException;
import com.roczyno.projectservice.external.chat.Chat;
import com.roczyno.projectservice.external.chat.ChatResponse;
import com.roczyno.projectservice.external.chat.ChatService;
import com.roczyno.projectservice.external.user.UserResponse;
import com.roczyno.projectservice.external.user.UserService;
import com.roczyno.projectservice.model.Project;
import com.roczyno.projectservice.repository.ProjectRepository;
import com.roczyno.projectservice.request.ProjectRequest;
import com.roczyno.projectservice.response.ProjectResponse;
import com.roczyno.projectservice.service.ProjectService;
import com.roczyno.projectservice.util.ProjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
	private final ProjectRepository projectRepository;
	private final ProjectMapper mapper;
	private final UserService userService;
	private final ChatService chatService;

	@Transactional
	@Override
	public ProjectResponse createProject(ProjectRequest req, String jwt) {
		UserResponse user = userService.getUserProfile(jwt);
		Project project = Project.builder()
				.name(req.name())
				.description(req.description())
				.tags(req.tags())
				.category(req.category())
				.createdAt(LocalDateTime.now())
				.userId(user.id())
				.build();
		Project savedProject = projectRepository.save(project);

		Chat chat= new Chat();
		chat.setCreatedAt(LocalDateTime.now());
		chat.setProjectId(project.getId());

		ChatResponse projectChat = chatService.createChat(chat, savedProject.getId());
		savedProject.setChatId(projectChat.id());
		Project updatedProject = projectRepository.save(savedProject);
		return mapper.mapToProjectResponse(updatedProject);
	}


	@Override
	public ProjectResponse getProject(Integer projectId) {
		Project project= projectRepository.findById(projectId)
				.orElseThrow(()-> new ProjectException("project not found"));
		return mapper.mapToProjectResponse(project);
	}

	@Override
	public List<ProjectResponse> getProjectByTeam(String jwt, String category, String tag) {
		UserResponse user=userService.getUserProfile(jwt);
		List<Project> projects=projectRepository.findByTeamOrOwner(user.id(),user.id());
		if(category!=null){
			projects=projects.stream().filter(project -> project.getCategory().equals(category))
					.toList();
		}
		if(tag!=null){
			projects=projects.stream().filter(project -> project.getTags().contains(tag))
					.toList();
		}

		return projects.stream()
				.map(mapper::mapToProjectResponse)
				.toList();
	}

	@Override
	public String deleteProject(Integer projectId, String jwt) {
		UserResponse user=userService.getUserProfile(jwt);
		Project project= projectRepository.findById(projectId)
				.orElseThrow(()-> new ProjectException("project not found"));
		if(!project.getUserId().equals(user.id())){
			throw new ProjectException("Only the owner of the project can delete it");
		}
		projectRepository.delete(project);
		return "Project deleted successfully";
	}

	@Override
	public ProjectResponse updateProject(Integer projectId, ProjectRequest req, String jwt) {
		UserResponse user=userService.getUserProfile(jwt);
		Project project= projectRepository.findById(projectId)
				.orElseThrow(()-> new ProjectException("project not found"));
		if(!project.getUserId().equals(user.id())){
			throw new RuntimeException("Only the owner of the project can delete it");
		}
		if(req.name()!=null){
			project.setName(req.name());
		}
		if(req.description()!=null){
			project.setDescription(req.description());
		}
		if(req.tags()!=null){
			project.setTags(req.tags());
		}
		if(req.category()!=null){
			project.setCategory(req.category());
		}
		Project updatedProject= projectRepository.save(project);

		return mapper.mapToProjectResponse(updatedProject);
	}

	@Override
	@Transactional
	public String addUserToProject(Integer projectId, String jwt) {
		UserResponse user=userService.getUserProfile(jwt);
		Project project= projectRepository.findById(projectId)
				.orElseThrow(()-> new ProjectException("project not found"));
		if(!project.getUserId().equals(user.id())){
			throw new RuntimeException("Only the owner of the project can add a user to it");
		}
		if(project.getTeamMemberIds().contains(user.id())){
			throw new ProjectException("User already part of team");
		}
		project.getTeamMemberIds().add(user.id());
		chatService.addUserToChat(projectId,user.id());
		return "user added successfully";
	}

	@Override
	public String removeUserFromProject(Integer projectId, String jwt) {
		UserResponse user=userService.getUserProfile(jwt);
		Project project= projectRepository.findById(projectId)
				.orElseThrow(()-> new ProjectException("project not found"));
		if(!project.getUserId().equals(user.id())){
			throw new ProjectException("Only the owner of the project can add a user to it");
		}
		if(!project.getTeamMemberIds().contains(user.id())){
			throw new ProjectException("User not part of group or already removed form group");
		}
		project.getTeamMemberIds().remove(user.id());
		return "User removed successfully";
	}

	@Override
	public List<ProjectResponse> searchProject(String keyword, String jwt) {
		return List.of();
	}
}
