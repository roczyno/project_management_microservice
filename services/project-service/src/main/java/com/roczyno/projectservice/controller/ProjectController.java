package com.roczyno.projectservice.controller;

import com.roczyno.projectservice.external.user.UserResponse;
import com.roczyno.projectservice.request.ProjectRequest;
import com.roczyno.projectservice.response.ProjectResponse;
import com.roczyno.projectservice.service.ProjectService;
import jakarta.validation.Valid;
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
@RequestMapping("/api/v1/project")
@RequiredArgsConstructor
public class ProjectController {
	private final ProjectService projectService;

	@PostMapping("/create")
	public ResponseEntity<ProjectResponse> addProject(@Valid @RequestBody ProjectRequest req, @RequestHeader("Authorization") String jwt) {
		return  ResponseEntity.ok(projectService.createProject(req,jwt));
	}
	@GetMapping("/{id}")
	public ResponseEntity<ProjectResponse>getProject(@PathVariable Integer id){
		return  ResponseEntity.ok(projectService.getProject(id));
	}
	@GetMapping("/all")
	public ResponseEntity<List<ProjectResponse>> getAllProjects(@RequestHeader("Authorization") String jwt
			, @RequestParam(required = false) String category, @RequestParam(required = false) String tag){
		return  ResponseEntity.ok(projectService.getProjectByTeam(jwt,category,tag));
	}
	@PutMapping("/{projectId}")
	public ResponseEntity<ProjectResponse> updateProject(@PathVariable Integer projectId,
												@Valid @RequestBody ProjectRequest projectRequest,
												@RequestHeader("Authorization") String jwt) {

		return  ResponseEntity.ok(projectService.updateProject(projectId,projectRequest,jwt));
	}
	@DeleteMapping("/{projectId}")
	public ResponseEntity<String> deleteProject(@PathVariable Integer projectId,
												@RequestHeader("Authorization") String jwt){
		return  ResponseEntity.ok(projectService.deleteProject(projectId,jwt));
	}

	@PostMapping("/add/{projectId}")
	public ResponseEntity<String> addUserToProject(@PathVariable Integer projectId,
												   @RequestHeader("Authorization") String jwt){
		return  ResponseEntity.ok(projectService.addUserToProject(projectId, jwt));
	}
	@PostMapping("/remove/{projectId}/user/{userId}")
	public ResponseEntity<String> removeUserFromProject(@PathVariable Integer projectId,
														@PathVariable Integer userId,
														@RequestHeader("Authorization") String jwt)
	{
		return  ResponseEntity.ok(projectService.removeUserFromProject(projectId,userId,jwt));
	}

	@GetMapping("/team/{projectId}")
	public ResponseEntity<List<UserResponse>> getProjectTeam(@PathVariable Integer projectId,
															 @RequestHeader("Authorization") String jwt){
		return ResponseEntity.ok(projectService.findProjectTeamByProjectId(projectId,jwt));
	}
	@GetMapping("/search")
	public ResponseEntity<List<ProjectResponse>> searchProject(@RequestParam(required = false) String tag,
												@RequestHeader("Authorization") String jwt) {
		return  ResponseEntity.ok(projectService.searchProject(tag,jwt));
	}

}
