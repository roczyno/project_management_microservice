package com.roczyno.subscriptionservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Subscription {
	@Id
	@GeneratedValue
	private Integer id;
	private LocalDateTime subscriptionStartDate;
	private LocalDateTime subscriptionEndDate;
	@Builder.Default
	private boolean isSubscriptionValid=false;
	private PlanType planType;
	private Integer userId;
	private LocalDateTime createdAt;
}
