package com.roczyno.userservice.external.subscription;

import java.time.LocalDateTime;

public record SubscriptionResponse(
		Integer id,
		LocalDateTime subscriptionStartDate,
		LocalDateTime subscriptionEndDate,
		boolean isSubscriptionValid,
		PlanType planType,
		LocalDateTime createdAt
) {
}
