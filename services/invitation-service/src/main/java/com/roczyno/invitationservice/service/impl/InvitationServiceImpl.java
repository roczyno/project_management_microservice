package com.roczyno.invitationservice.service.impl;

import com.roczyno.invitationservice.external.project.ProjectResponse;
import com.roczyno.invitationservice.external.project.ProjectService;
import com.roczyno.invitationservice.external.user.UserResponse;
import com.roczyno.invitationservice.external.user.UserService;
import com.roczyno.invitationservice.kafka.InvitationConfirmation;
import com.roczyno.invitationservice.kafka.InvitationProducer;
import com.roczyno.invitationservice.model.Invitation;
import com.roczyno.invitationservice.repository.InvitationRepository;
import com.roczyno.invitationservice.service.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvitationServiceImpl implements InvitationService {
	private final InvitationRepository invitationRepository;
	private final ProjectService projectService;
	private final UserService userService;
	private final InvitationProducer invitationProducer;

	@Override
	public String sendInvitation(String email, Integer projectId,String jwt) {
		String token= UUID.randomUUID().toString();

		UserResponse user=userService.getUserProfile(jwt);
		ProjectResponse project=projectService.getProject(projectId);
		Invitation invitation=Invitation.builder()
				.email(email)
				.projectId(projectId)
				.token(token)
				.email(email)
				.senderId(user.id())
				.build();
		invitationRepository.save(invitation);

		invitationProducer.sendInviteConfirmation(new InvitationConfirmation(
				email,
				user.username(),
				"Invite to Project Team",
				project.name(),
				"khkhkhkh"
		));
		return "Invitation sent successfully";
	}

	@Override
	public Invitation acceptInvitation(String token, String jwt) {
		var invitation=invitationRepository.findByToken(token);
		UserResponse user=userService.getUserProfile(jwt);
		if(!token.equals(invitation.getToken())){
			throw new RuntimeException();
		}
		projectService.addUserToProject(invitation.getProjectId(), jwt);
		return invitation;
	}
}
