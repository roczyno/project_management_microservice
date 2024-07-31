package com.roczyno.projectservice.service.impl;

import com.roczyno.projectservice.model.Project;
import com.roczyno.projectservice.repository.ProjectRepository;
import com.roczyno.projectservice.request.ProjectRequest;
import com.roczyno.projectservice.response.ProjectResponse;
import com.roczyno.projectservice.service.ProjectService;
import com.roczyno.projectservice.util.ProjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
	private final ProjectRepository projectRepository;
	private final ProjectMapper mapper;

	@Override
	public ProjectResponse createProject(ProjectRequest req, Integer userId) {
		Project project= Project.builder()
				.name(req.name())
				.description(req.description())
				.tags(req.tags())
				.category(req.category())
				.createdAt(LocalDateTime.now())
				.userId(userId)
				.build();
		return mapper.mapToProjectResponse(project);
	}

	@Override
	public ProjectResponse getProject(Integer projectId) {
		Project project= projectRepository.findById(projectId)
				.orElseThrow(()-> new RuntimeException("project not found"));
		return mapper.mapToProjectResponse(project);
	}

	@Override
	public List<ProjectResponse> getProjectByTeam(Integer userId, String category, String tag) {
		List<Project> projects=projectRepository.findByTeamOrOwner(userId,userId);
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
	public String deleteProject(Integer projectId, Integer userId) {
		Project project= projectRepository.findById(projectId)
				.orElseThrow(()-> new RuntimeException("project not found"));
		if(!project.getUserId().equals(userId)){
			throw new RuntimeException("Only the owner of the project can delete it");
		}
		projectRepository.delete(project);
		return "Project deleted successfully";
	}

	@Override
	public ProjectResponse updateProject(Integer projectId, ProjectRequest req, Integer userId) {
		Project project= projectRepository.findById(projectId)
				.orElseThrow(()-> new RuntimeException("project not found"));
		if(!project.getUserId().equals(userId)){
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
	public String addUserToProject(Integer projectId, Integer userId) {
		Project project= projectRepository.findById(projectId)
				.orElseThrow(()-> new RuntimeException("project not found"));
		if(!project.getUserId().equals(userId)){
			throw new RuntimeException("Only the owner of the project can add a user to it");
		}
		if(project.getTeamMemberIds().contains(userId)){
			throw new RuntimeException("User already part of team");
		}
		project.getTeamMemberIds().add(userId);
		return "user added successfully";
	}

	@Override
	public String removeUserFromProject(Integer projectId, Integer userId) {
		Project project= projectRepository.findById(projectId)
				.orElseThrow(()-> new RuntimeException("project not found"));
		if(!project.getUserId().equals(userId)){
			throw new RuntimeException("Only the owner of the project can add a user to it");
		}
		if(!project.getTeamMemberIds().contains(userId)){
			throw new RuntimeException("User not part of group or already removed form group");
		}
		project.getTeamMemberIds().remove(userId);
		return "User removed successfully";
	}

	@Override
	public List<ProjectResponse> searchProject(String keyword, Integer userId) {
		return List.of();
	}
}
