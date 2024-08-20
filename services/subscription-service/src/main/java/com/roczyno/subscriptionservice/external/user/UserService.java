package com.roczyno.subscriptionservice.external.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
@Service
@FeignClient(name = "USER-SERVICE",url = "http://localhost:8090")
public interface UserService {
	@PutMapping("/api/v1/user/decrease/project-size/{userId}")
    String decreaseUserProjectSize(@PathVariable Integer userId);
}
