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
				project.getChatId(),
				project.getUserId()
		);
	}

	public Project mapToProject(ProjectResponse projectResponse) {
		return Project.builder()
				.id(projectResponse.id())
				.name(projectResponse.name())
				.description(projectResponse.description())
				.category(projectResponse.category())
				.tags(projectResponse.tags())
				.chatId(projectResponse.chatId())
				.userId(projectResponse.userId())
				.build();
	}
}
