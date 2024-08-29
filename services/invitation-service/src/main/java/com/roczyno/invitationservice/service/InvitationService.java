package com.roczyno.invitationservice.service;

public interface InvitationService {
	String sendInvitation(String email,Integer projectId,String jwt);
	void acceptInvitation(String token,  String jwt) ;
}
