package com.roczyno.invitationservice.controller;

import com.roczyno.invitationservice.request.InviteRequest;
import com.roczyno.invitationservice.service.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/invite")
@RequiredArgsConstructor
public class InvitationController {
	private final InvitationService invitationService;

	@PostMapping("/send")
	public ResponseEntity<String> sendInvite(@RequestBody InviteRequest req, @RequestHeader("Authorization") String jwt){
		return ResponseEntity.ok(invitationService.sendInvitation(req.email(), req.projectId(), jwt));
	}
	@PostMapping("/accept")
	public ResponseEntity<Object> acceptInviteToProject(@RequestHeader("Authorization") String jwt,
														@RequestParam String token) {
		return ResponseEntity.ok(invitationService.acceptInvitation(token, jwt));
	}

}
