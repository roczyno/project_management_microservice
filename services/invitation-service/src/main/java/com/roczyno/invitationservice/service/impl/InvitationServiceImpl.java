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
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
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

		log.info("this is project {}",project);
		if(project == null){
			throw  new IllegalStateException("Project is null");
		}
		Invitation invitation=Invitation.builder()
				.email(email)
				.projectId(projectId)
				.token(token)
				.email(email)
				.senderId(user.id())
				.createdAt(LocalDateTime.now())
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
	public void acceptInvitation(String token, String jwt) {
		var invitation=invitationRepository.findByToken(token);
		projectService.addUserToProject(invitation.getProjectId(), jwt);

	}
}
