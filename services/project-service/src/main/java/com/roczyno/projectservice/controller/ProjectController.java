package com.roczyno.projectservice.controller;

import com.roczyno.projectservice.request.ProjectRequest;
import com.roczyno.projectservice.service.ProjectService;
import com.roczyno.projectservice.util.ResponseHandler;
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
@RequestMapping("/api/v1/project")
@RequiredArgsConstructor
public class ProjectController {
	private final ProjectService projectService;

	@PostMapping("/create")
	public ResponseEntity<Object> addProject(@RequestBody ProjectRequest req, @RequestHeader("Authorization") String jwt) {
		return ResponseHandler.successResponse(projectService.createProject(req,jwt),HttpStatus.OK);
	}
	@GetMapping("/{id}")
	public ResponseEntity<Object>getProject(@PathVariable Integer id){
		return ResponseHandler.successResponse(projectService.getProject(id),HttpStatus.OK);
	}
	@GetMapping("/all")
	public ResponseEntity<Object> getAllProjects(@RequestHeader("Authorization") String jwt
			, @RequestParam(required = false) String category, @RequestParam(required = false) String tag){
		return ResponseHandler.successResponse(projectService.getProjectByTeam(jwt,category,tag),HttpStatus.OK);
	}
	@PutMapping("/{projectId}")
	public ResponseEntity<Object> updateProject(@PathVariable Integer projectId,
												@RequestBody ProjectRequest projectRequest,
												@RequestHeader("Authorization") String jwt) {

		return ResponseHandler.successResponse(projectService.updateProject(projectId,projectRequest,jwt),HttpStatus.OK);
	}
	@DeleteMapping("/{projectId}")
	public ResponseEntity<Object> deleteProject(@PathVariable Integer projectId,
												@RequestHeader("Authorization") String jwt){
		return ResponseHandler.successResponse(projectService.deleteProject(projectId,jwt),HttpStatus.OK);
	}

	@PostMapping("/add/{projectId}")
	public ResponseEntity<Object> addUserToProject(@PathVariable Integer projectId,
												   @RequestHeader("Authorization") String jwt){
		return ResponseHandler.successResponse(projectService.addUserToProject(projectId, jwt),HttpStatus.OK);
	}
	@PostMapping("/remove/{projectId}")
	public ResponseEntity<Object> removeUserFromProject(@PathVariable Integer projectId,
														@RequestHeader("Authorization") String jwt)
	{
		return ResponseHandler.successResponse(projectService.removeUserFromProject(projectId,jwt),HttpStatus.OK);
	}
	@GetMapping("/search")
	public ResponseEntity<Object> searchProject(@RequestParam(required = false) String tag,
												@RequestHeader("Authorization") String jwt) {
		return ResponseHandler.successResponse(projectService.searchProject(tag,jwt),HttpStatus.OK);
	}
}
