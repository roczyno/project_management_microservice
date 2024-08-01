package com.roczyno.projectservice.service;

import com.roczyno.projectservice.request.ProjectRequest;
import com.roczyno.projectservice.response.ProjectResponse;

import java.util.List;

public interface ProjectService {
	ProjectResponse createProject(ProjectRequest project, String jwt);

	ProjectResponse getProject(Integer projectId);

	List<ProjectResponse> getProjectByTeam(String jwt, String category, String tag);

	String deleteProject(Integer projectId, String jwt);

	ProjectResponse updateProject(Integer projectId, ProjectRequest project, String jwt);

	String addUserToProject(Integer projectId, String jwt);

	String removeUserFromProject(Integer projectId, String jwt);

	List<ProjectResponse> searchProject(String keyword,String jwt);
}
