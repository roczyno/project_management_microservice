package com.roczyno.projectservice.service;

import com.roczyno.projectservice.request.ProjectRequest;
import com.roczyno.projectservice.response.ProjectResponse;

import java.util.List;

public interface ProjectService {
	ProjectResponse createProject(ProjectRequest project, Integer userId);

	ProjectResponse getProject(Integer projectId);

	List<ProjectResponse> getProjectByTeam(Integer userId, String category, String tag);

	String deleteProject(Integer projectId, Integer userId);

	ProjectResponse updateProject(Integer projectId, ProjectRequest project, Integer userId);

	String addUserToProject(Integer projectId, Integer userId);

	String removeUserFromProject(Integer projectId, Integer userId);

	List<ProjectResponse> searchProject(String keyword,Integer userId);
}
