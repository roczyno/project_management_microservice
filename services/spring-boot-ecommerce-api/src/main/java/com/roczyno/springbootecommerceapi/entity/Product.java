package com.roczyno.springbootecommerceapi.entity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String description;
	private int price;
	private int discountPrice;
	private int discountPricePercent;
	private int quantity;
	private String brand;
	private String color;
	@ElementCollection(fetch = FetchType.EAGER)
	private Set<String> sizes= new HashSet<>();
	private String imageUrl;
	private int numOfRatings;
	@ManyToOne
	private Category category;
	private LocalDateTime createdAt;
}
