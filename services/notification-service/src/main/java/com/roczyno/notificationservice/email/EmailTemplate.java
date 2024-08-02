package com.roczyno.notificationservice.email;

import lombok.Getter;

@Getter
public enum EmailTemplate {
	ACTIVATE_ACCOUNT("activate_account"),
	SEND_INVITE("Invite");
	private final String name;
	EmailTemplate(String name) {
		this.name = name;
	}
}
