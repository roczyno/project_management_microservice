package com.roczyno.userservice.external.subscription;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "SUBSCRIPTION-SERVICE",url = "http://localhost:8088")
public interface SubscriptionService {
	@PostMapping("/api/v1/subscription/user/{userId}")
	SubscriptionResponse createSubscription(@PathVariable Integer userId);
}
