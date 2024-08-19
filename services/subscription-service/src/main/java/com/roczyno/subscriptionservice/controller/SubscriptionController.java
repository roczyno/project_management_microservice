package com.roczyno.subscriptionservice.controller;

import com.roczyno.subscriptionservice.model.PlanType;
import com.roczyno.subscriptionservice.response.SubscriptionResponse;
import com.roczyno.subscriptionservice.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/subscription")
@RequiredArgsConstructor
public class SubscriptionController {
	private final SubscriptionService subscriptionService;
	@PostMapping("/user/{userId}")
	public ResponseEntity<SubscriptionResponse> createSubscription(@PathVariable Integer userId) {
		return ResponseEntity.ok(subscriptionService.createSubscription(userId));
	}

	@GetMapping("/{userId}")
	public ResponseEntity<Object> getUserSubscription(@PathVariable Integer userId) {
		return ResponseEntity.ok(subscriptionService.getUserSubscription(userId));
	}

	@PutMapping("/{userId}/upgrade")
	public ResponseEntity<Object> upgradeSubscription(@PathVariable Integer userId, @RequestParam PlanType planType) {
		return ResponseEntity.ok(subscriptionService.upgradeSubscription(userId, planType));
	}

}
