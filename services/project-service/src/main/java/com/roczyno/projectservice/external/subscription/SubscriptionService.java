package com.roczyno.projectservice.external.subscription;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@FeignClient(name = "SUBSCRIPTION-SERVICE",url = "http://localhost:8088")
public interface SubscriptionService {
	@GetMapping("/api/v1/subscription/user/{userId}")
	SubscriptionResponse getUserSubscription(@PathVariable Integer userId) ;

}
