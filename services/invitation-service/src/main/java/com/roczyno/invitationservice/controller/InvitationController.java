package com.roczyno.invitationservice.controller;

import com.roczyno.invitationservice.request.InviteRequest;
import com.roczyno.invitationservice.service.InvitationService;
import com.roczyno.invitationservice.util.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
	public ResponseEntity<Object> sendInvite(@RequestBody InviteRequest req, @RequestHeader("Authorization") String jwt){
		return ResponseHandler.successResponse(invitationService.sendInvitation(req.email(), req.projectId(), jwt), HttpStatus.OK);
	}
	@PostMapping("/accept")
	public ResponseEntity<Object> acceptInviteToProject(@RequestHeader("Authorization") String jwt,
														@RequestParam String token) {
		return ResponseHandler.successResponse(invitationService.acceptInvitation(token, jwt),HttpStatus.OK);
	}

}
