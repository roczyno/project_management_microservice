package com.roczyno.subscriptionservice.response;

import com.roczyno.subscriptionservice.model.PlanType;

import java.time.LocalDateTime;

public record SubscriptionResponse(
		Integer id,
		LocalDateTime subscriptionStartDate,
		LocalDateTime subscriptionEndDate,
		boolean isSubscriptionValid,
		PlanType planType,
		LocalDateTime createdAt) {
}
