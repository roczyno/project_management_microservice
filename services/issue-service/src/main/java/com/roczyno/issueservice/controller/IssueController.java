package com.roczyno.issueservice.controller;

import com.roczyno.issueservice.request.IssueRequest;
import com.roczyno.issueservice.service.IssueService;
import com.roczyno.issueservice.util.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/issue")
public class IssueController {
	private final IssueService issueService;

	@PostMapping("/project/{projectId}")
	public ResponseEntity<Object> createIssue(@RequestBody IssueRequest req, @PathVariable Integer projectId,
													 @RequestHeader("Authorization") String jwt){
		return ResponseHandler.successResponse(issueService.createIssue(req,jwt,projectId), HttpStatus.OK);
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteIssue(@PathVariable Integer id,@RequestHeader("Authorization") String jwt){
		return ResponseHandler.successResponse(issueService.deleteIssue(id,jwt),HttpStatus.OK);
	}
	@GetMapping("/project/{projectId}")
	public ResponseEntity<Object> getIssues(@PathVariable Integer projectId){
		return ResponseHandler.successResponse(issueService.getIssuesByProjectId(projectId),HttpStatus.OK);
	}
	@GetMapping("/{id}")
	public ResponseEntity<Object> getIssue(@PathVariable Integer id){
		return ResponseHandler.successResponse(issueService.getIssue(id),HttpStatus.OK);
	}
	@PostMapping("/{issueId}/user/{userId}")
	public ResponseEntity<Object> addUserToIssue(@PathVariable Integer issueId,@PathVariable Integer userId){
		return ResponseHandler.successResponse(issueService.addUserToIssue(userId,issueId),HttpStatus.OK);
	}
	@PutMapping("/status/{issueId}")
	public ResponseEntity<Object> updateIssueStatus(@RequestParam String status,@PathVariable Integer issueId){
		return ResponseHandler.successResponse(issueService.updateIssueStatus(issueId,status),HttpStatus.OK);
	}
	@PutMapping("/{issueId}")
	public ResponseEntity<Object> updateIssue(@RequestBody IssueRequest req,@PathVariable Integer issueId){
		return ResponseHandler.successResponse(issueService.updateIssue(issueId,req),HttpStatus.OK);
	}

}
