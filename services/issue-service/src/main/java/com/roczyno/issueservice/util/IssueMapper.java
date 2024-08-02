package com.roczyno.issueservice.util;

import com.roczyno.issueservice.model.Issue;
import com.roczyno.issueservice.response.IssueResponse;
import org.springframework.stereotype.Service;

@Service
public class IssueMapper {
	public IssueResponse toIssueResponse(Issue req) {
		return new IssueResponse(
				req.getId(),
				req.getTitle(),
				req.getDescription(),
				req.getStatus(),
				req.getPriority(),
				req.getDueDate(),
				req.getTags(),
				req.getAssigneeId(),
				req.getCreatorId(),
				req.getProjectId()

		);
	}

	public Issue toIssue(IssueResponse res) {
		return Issue.builder()
				.id(res.id())
				.title(res.title())
				.description(res.description())
				.status(res.status())
				.priority(res.status())
				.dueDate(res.dueDate())
				.tags(res.tags())
				.assigneeId(res.assigneeId())
				.creatorId(res.creatorId())
				.projectId(res.projectId())
				.build();
	}
}
