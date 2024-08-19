package com.roczyno.subscriptionservice.util;

import com.roczyno.subscriptionservice.model.Subscription;
import com.roczyno.subscriptionservice.response.SubscriptionResponse;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionResponseMapper {
	public SubscriptionResponse toSubscriptionResponse(Subscription req) {
		return new SubscriptionResponse(
				req.getId(),
				req.getSubscriptionStartDate(),
				req.getSubscriptionStartDate(),
				req.isSubscriptionValid(),
				req.getPlanType(),
				req.getCreatedAt()
		);
	}

	public Subscription toSubscription(SubscriptionResponse userSubscription) {
		return Subscription.builder()
				.id(userSubscription.id())
				.subscriptionStartDate(userSubscription.subscriptionStartDate())
				.subscriptionEndDate(userSubscription.subscriptionEndDate())
				.isSubscriptionValid(userSubscription.isSubscriptionValid())
				.planType(userSubscription.planType())
				.createdAt(userSubscription.createdAt())
				.build();
	}
}
