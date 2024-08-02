package com.roczyno.issueservice.service;

import com.roczyno.issueservice.model.Issue;
import com.roczyno.issueservice.request.IssueRequest;
import com.roczyno.issueservice.response.IssueResponse;

import java.util.List;

public interface IssueService {
	IssueResponse createIssue(IssueRequest issue, String jwt, Integer projectId);
	IssueResponse getIssue(Integer id);
	String deleteIssue(Integer id, String jwt);
	List<IssueResponse> getIssuesByProjectId(Integer projectId);
	IssueResponse addUserToIssue(Integer userId, Integer issueId);
	IssueResponse updateIssueStatus(Integer issueId,String status);
	IssueResponse updateIssue(Integer IssueId,IssueRequest req);
}
