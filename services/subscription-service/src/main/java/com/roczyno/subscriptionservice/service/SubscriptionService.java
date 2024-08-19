package com.roczyno.subscriptionservice.service;

import com.roczyno.subscriptionservice.model.PlanType;
import com.roczyno.subscriptionservice.response.SubscriptionResponse;

public interface SubscriptionService {
	SubscriptionResponse createSubscription(Integer userId);
	SubscriptionResponse getUserSubscription(Integer userId);
	SubscriptionResponse upgradeSubscription(Integer userId, PlanType planType);
}
