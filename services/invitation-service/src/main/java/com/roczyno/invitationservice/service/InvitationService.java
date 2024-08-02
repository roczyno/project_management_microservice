package com.roczyno.invitationservice.service;

import com.roczyno.invitationservice.model.Invitation;

public interface InvitationService {
	String sendInvitation(String email,Integer projectId,String jwt);
	Invitation acceptInvitation(String token,  String jwt) ;
}
