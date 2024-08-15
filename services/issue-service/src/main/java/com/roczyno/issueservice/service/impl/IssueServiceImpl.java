package com.roczyno.issueservice.service.impl;

import com.roczyno.issueservice.exception.IssueException;
import com.roczyno.issueservice.external.project.ProjectResponse;
import com.roczyno.issueservice.external.project.ProjectService;
import com.roczyno.issueservice.external.user.UserResponse;
import com.roczyno.issueservice.external.user.UserService;
import com.roczyno.issueservice.kafka.IssueConfirmation;
import com.roczyno.issueservice.kafka.IssueProducer;
import com.roczyno.issueservice.model.Issue;
import com.roczyno.issueservice.repository.IssueRepository;
import com.roczyno.issueservice.request.IssueRequest;
import com.roczyno.issueservice.response.IssueResponse;
import com.roczyno.issueservice.service.IssueService;
import com.roczyno.issueservice.util.IssueMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IssueServiceImpl implements IssueService {
	private final IssueRepository issueRepository;
	private final UserService userService;
	private final ProjectService projectService;
	private final IssueMapper mapper;
	private final IssueProducer issueProducer;

	@Override
	public IssueResponse createIssue(IssueRequest req, String jwt, Integer projectId) {
		UserResponse user=userService.getUserProfile(jwt);
		Issue issue=Issue.builder()
				.title(req.title())
				.description(req.description())
				.status(req.status())
				.projectId(projectId)
				.priority(req.priority())
				.dueDate(req.dueDate())
				.tags(req.tags())
				.creatorId(user.id())
				.build();
		return mapper.toIssueResponse(issueRepository.save(issue));
	}

	@Override
	public IssueResponse getIssue(Integer id) {
		Issue issue= issueRepository.findById(id)
				.orElseThrow(()-> new IssueException("Issue not found"));
		return mapper.toIssueResponse(issue);
	}

	@Override
	public String deleteIssue(Integer id, String jwt) {
		UserResponse user=userService.getUserProfile(jwt);
		IssueResponse issueResponse=getIssue(id);
		if(!issueResponse.assigneeId().equals(user.id())){
			throw new IssueException("Only the owner can delete the issue");
		}
		Issue issue=mapper.toIssue(issueResponse);
		issueRepository.delete(issue);
		return "Issue deleted successfully";
	}

	@Override
	public List<IssueResponse> getIssuesByProjectId(Integer projectId) {
		List<Issue> issues= issueRepository.findByProjectId(projectId);
		return issues.stream()
				.map(mapper::toIssueResponse)
				.toList();
	}

	@Override
	public IssueResponse addUserToIssue(Integer userId, Integer issueId,String jwt) {
		Issue issue=issueRepository.findById(issueId).orElseThrow();
		issue.setAssigneeId(userId);
		Issue savedIssue=issueRepository.save(issue);
		UserResponse user=userService.getUserById(userId, jwt);
		ProjectResponse project=projectService.getProject(savedIssue.getProjectId());

		//send email notification
		issueProducer.sendIssueConfirmation(new IssueConfirmation(
				savedIssue.getId(),
				savedIssue.getTitle(),
				savedIssue.getDescription(),
				savedIssue.getStatus(),
				savedIssue.getPriority(),
				savedIssue.getDueDate(),
				project.name(),
				user.email(),
				"Assigned Issue",
				"Admin"
		));
		return mapper.toIssueResponse(savedIssue);

	}

	@Override
	public IssueResponse updateIssueStatus(Integer issueId, String status) {
		Issue issue=issueRepository.findById(issueId).orElseThrow();
		issue.setStatus(status);
		Issue updatedIssue=issueRepository.save(issue);
		return mapper.toIssueResponse(updatedIssue);
	}

	@Override
	public IssueResponse updateIssue(Integer issueId, IssueRequest req) {
		Issue issue=issueRepository.findById(issueId).orElseThrow();
		if(req.title()!=null){
			issue.setTitle(req.title());
		}
		if(req.description()!=null){
			issue.setDescription(req.description());
		}
		if(req.status()!=null){
			issue.setStatus(req.status());
		}
		if(req.priority()!=null){
			issue.setPriority(req.priority());
		}
		if(req.dueDate()!=null){
			issue.setDueDate(req.dueDate());
		}
		if(req.tags()!=null){
			issue.setTags(req.tags());
		}
		return null;
	}
}
