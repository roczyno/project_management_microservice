package com.roczyno.subscriptionservice.service.impl;

import com.roczyno.subscriptionservice.external.user.UserService;
import com.roczyno.subscriptionservice.model.PlanType;
import com.roczyno.subscriptionservice.model.Subscription;
import com.roczyno.subscriptionservice.repository.SubscriptionRepository;
import com.roczyno.subscriptionservice.response.SubscriptionResponse;
import com.roczyno.subscriptionservice.service.SubscriptionService;
import com.roczyno.subscriptionservice.util.SubscriptionResponseMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {
	private final SubscriptionRepository subscriptionRepository;
	private final SubscriptionResponseMapper mapper;
	private final UserService userService;

	private static final int BASIC_PLAN_DAYS = 12;
	private static final int STANDARD_PLAN_DAYS = 30;
	private static final int PREMIUM_PLAN_DAYS = 365;
	@Override
	public SubscriptionResponse createSubscription(Integer userId) {
		Subscription subscription=Subscription.builder()
				.subscriptionStartDate(LocalDateTime.now())
				.subscriptionEndDate(LocalDateTime.now().plusDays(BASIC_PLAN_DAYS))
				.isSubscriptionValid(true)
				.createdAt(LocalDateTime.now())
				.planType(PlanType.FREE)
				.userId(userId)
				.build();
		Subscription saveSubscription=subscriptionRepository.save(subscription);
		return mapper.toSubscriptionResponse(saveSubscription);
	}

	@Override
	public SubscriptionResponse getUserSubscription(Integer userId) {
		Subscription subscription= subscriptionRepository.findByUserId(userId);
		if(subscription==null){
			throw new RuntimeException();
		}
		if (!isValidSubscription(userId)) {
			subscription.setPlanType(PlanType.FREE);
			subscription.setSubscriptionStartDate(LocalDateTime.now());
			subscription.setSubscriptionEndDate(LocalDateTime.now().plusDays(BASIC_PLAN_DAYS));
			subscription = subscriptionRepository.save(subscription);
		}
		return mapper.toSubscriptionResponse(subscription);
	}

	@Override
	@Transactional
	@CircuitBreaker(name = "userBreaker",fallbackMethod = "userBreakerFallback")
	@Retry(name = "userBreaker",fallbackMethod = "userBreakerFallback")
	@RateLimiter(name = "userBreaker",fallbackMethod = "userBreakerFallback")
	public SubscriptionResponse upgradeSubscription(Integer userId, PlanType planType) {
		Subscription subscription=mapper.toSubscription(getUserSubscription(userId));
		subscription.setPlanType(planType);
		subscription.setSubscriptionStartDate(LocalDateTime.now());

		int duration = switch (planType) {
			case FREE -> BASIC_PLAN_DAYS;
			case MONTHLY -> STANDARD_PLAN_DAYS;
			case ANNUALLY -> PREMIUM_PLAN_DAYS;
		};
		subscription.setSubscriptionEndDate(LocalDateTime.now().plusDays(duration));
		var upgradedSubscription=subscriptionRepository.save(subscription);
		userService.decreaseUserProjectSize(userId);
		return mapper.toSubscriptionResponse(upgradedSubscription);
	}

	public List<String> userBreakerFallBack(Exception e) {
		log.error("Subscription service failed: {}", e.getMessage(), e);
		List<String> list = new ArrayList<>();
		list.add("Subscription Service not available");
		return list;
	}


	private boolean isValidSubscription(Integer userId) {
		Subscription subscription = subscriptionRepository.findByUserId(userId);
		LocalDateTime endDate = subscription.getSubscriptionEndDate();
		subscription.setSubscriptionValid(true);
		if( !endDate.isBefore(LocalDateTime.now())){
			subscriptionRepository.save(subscription);
			return true;
		}
		return false;
	}
}
