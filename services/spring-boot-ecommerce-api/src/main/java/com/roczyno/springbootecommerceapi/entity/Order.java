package com.roczyno.springbootecommerceapi.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	private User user;
	@OneToMany(cascade = CascadeType.ALL)
	private List<OrderItem> orderItems= new ArrayList<>();
	private LocalDateTime orderDate;
	private LocalDateTime deliveryDate;
	@Embedded
	private Address shippingAddress;
	private double totalPrice;
	private Integer totalDiscountedPrice;
	private Integer discount;
	private String orderStatus;
	private int totalItems;
	private LocalDateTime createdAt;
}
