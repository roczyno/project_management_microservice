package com.roczyno.subscriptionservice.controller;

import com.roczyno.subscriptionservice.model.PlanType;
import com.roczyno.subscriptionservice.request.PaymentRequest;
import com.roczyno.subscriptionservice.request.VerifyTransactionRequest;
import com.roczyno.subscriptionservice.response.PaymentResponse;
import com.roczyno.subscriptionservice.response.SubscriptionResponse;
import com.roczyno.subscriptionservice.service.PaymentService;
import com.roczyno.subscriptionservice.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/subscription")
@RequiredArgsConstructor
public class SubscriptionController {
	private final SubscriptionService subscriptionService;
	private final PaymentService paymentService;
	@PostMapping("/user/{userId}")
	public ResponseEntity<SubscriptionResponse> createSubscription(@PathVariable Integer userId) {
		return ResponseEntity.ok(subscriptionService.createSubscription(userId));
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<SubscriptionResponse> getUserSubscription(@PathVariable Integer userId) {
		return ResponseEntity.ok(subscriptionService.getUserSubscription(userId));
	}

	@PutMapping("/{userId}/upgrade")
	public ResponseEntity<SubscriptionResponse> upgradeSubscription(@PathVariable Integer userId, @RequestParam PlanType planType) {
		return ResponseEntity.ok(subscriptionService.upgradeSubscription(userId, planType));
	}


	@PostMapping("/payment/initialize")
	public ResponseEntity<PaymentResponse> initializeTransaction(@RequestBody PaymentRequest request) {
		try {
			PaymentResponse response = paymentService.initializeTransaction(request);
			return ResponseEntity.ok(response);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(null);
		}
	}

	@PostMapping("/payment/verify")
	public ResponseEntity<String> verifyTransaction(@RequestBody VerifyTransactionRequest request) {
		try {
			String response = paymentService.verifyTransaction(request.reference());
			return ResponseEntity.ok(response);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

}
