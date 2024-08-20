package com.roczyno.subscriptionservice.request;

public record PaymentRequest(
		String email,String amount
) {
}
