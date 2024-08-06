package com.roczyno.notificationservice.email;

import lombok.Getter;

@Getter
public enum EmailTemplate {
	ACTIVATE_ACCOUNT("activate_account"),
	SEND_INVITE("Invite_to_project"),
	SEND_ISSUE("Issue_Invite");
	private final String name;
	EmailTemplate(String name) {
		this.name = name;
	}
}
