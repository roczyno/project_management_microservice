package com.roczyno.issueservice.external.project;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "PROJECT-SERVICE",url = "http://localhost:8080")
public interface ProjectService {
	@GetMapping("/api/v1/project/{id}")
	ProjectResponse getProject(@PathVariable Integer id);

	@GetMapping("/api/v1/project/add/{id}")
	ProjectResponse addUserToProject(@PathVariable Integer id,@RequestHeader("Authorization") String jwt);

}
