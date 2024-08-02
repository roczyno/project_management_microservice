package com.roczyno.issueservice.controller;

import com.roczyno.issueservice.request.IssueRequest;
import com.roczyno.issueservice.response.IssueResponse;
import com.roczyno.issueservice.service.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/issue")
public class IssueController {
	private final IssueService issueService;

	@PostMapping("/project/{projectId}")
	public ResponseEntity<IssueResponse> createIssue(@RequestBody IssueRequest req, @PathVariable Integer projectId,
													 @RequestHeader("Authorization") String jwt){
		return ResponseEntity.ok(issueService.createIssue(req,jwt,projectId));
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteIssue(@PathVariable Integer id,@RequestHeader("Authorization") String jwt){
		return ResponseEntity.ok(issueService.deleteIssue(id,jwt));
	}
	@GetMapping("/project/{projectId}")
	public ResponseEntity<List<IssueResponse>> getIssues(@PathVariable Integer projectId){
		return ResponseEntity.ok(issueService.getIssuesByProjectId(projectId));
	}
	@GetMapping("/{id}")
	public ResponseEntity<IssueResponse> getIssue(@PathVariable Integer id){
		return ResponseEntity.ok(issueService.getIssue(id));
	}
	@PostMapping("/{issueId}/user/{userId}")
	public ResponseEntity<IssueResponse> addUserToIssue(@PathVariable Integer issueId,@PathVariable Integer userId){
		return ResponseEntity.ok(issueService.addUserToIssue(userId,issueId));
	}
	@PutMapping("/{issueId}")
	public ResponseEntity<IssueResponse> updateIssueStatus(@RequestParam String status,@PathVariable Integer issueId){
		return ResponseEntity.ok(issueService.updateIssueStatus(issueId,status));
	}
	@PutMapping("/{issueId}")
	public ResponseEntity<IssueResponse> updateIssue(@RequestBody IssueRequest req,@PathVariable Integer issueId){
		return ResponseEntity.ok(issueService.updateIssue(issueId,req));
	}

}
