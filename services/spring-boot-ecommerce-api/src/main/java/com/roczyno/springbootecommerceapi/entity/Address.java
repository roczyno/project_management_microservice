package com.roczyno.springbootecommerceapi.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class Address {
	private String mobile;
	private String streetAddress;
	private String zipCode;
}
