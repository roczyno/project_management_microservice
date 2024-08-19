package com.roczyno.subscriptionservice.repository;

import com.roczyno.subscriptionservice.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription,Integer> {
	Subscription findByUserId(Integer userId);
}
