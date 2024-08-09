package com.roczyno.projectservice.util;

import com.roczyno.projectservice.model.Project;
import com.roczyno.projectservice.response.ProjectResponse;
import org.springframework.stereotype.Service;

@Service
public class ProjectMapper {
	public ProjectResponse mapToProjectResponse(Project project) {
		return new ProjectResponse(
				project.getId(),
				project.getName(),
				project.getDescription(),
				project.getCategory(),
				project.getTags(),
				project.getChatId()
		);
	}
}
