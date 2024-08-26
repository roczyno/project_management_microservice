package com.roczyno.springbootecommerceapi.request;

public record AddItemRequest(
		 String size,
		 int quantity,
		 Integer price
) {
}
