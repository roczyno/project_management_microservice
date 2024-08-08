package com.roczyno.userservice.controller;

import com.roczyno.userservice.request.AuthRequest;
import com.roczyno.userservice.request.ChangePasswordRequest;
import com.roczyno.userservice.request.PasswordResetRequest;
import com.roczyno.userservice.request.PasswordUpdateRequest;
import com.roczyno.userservice.request.RegistrationRequest;
import com.roczyno.userservice.response.AuthResponse;
import com.roczyno.userservice.service.AuthenticationService;
import com.roczyno.userservice.util.ResponseHandler;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
	private final AuthenticationService authenticationService;

	@PostMapping("/register")
	public ResponseEntity<Object> register(@RequestBody RegistrationRequest req) {
		return ResponseHandler.successResponse(authenticationService.register(req), HttpStatus.OK);
	}
	@PostMapping("/login")
	public ResponseEntity<Object> login(@RequestBody AuthRequest req) {
		return ResponseHandler.successResponse(authenticationService.login(req),HttpStatus.OK);
	}
	@GetMapping("/activate-account")
	public ResponseEntity<Object> confirm(@RequestParam String token) throws MessagingException {
		return ResponseHandler.successResponse(authenticationService.activateAccount(token),HttpStatus.OK);
	}
	@PostMapping("/forgot-password")
	public ResponseEntity<Object> initiateForgotPassword(@RequestBody PasswordResetRequest req) {
		return ResponseHandler.successResponse(authenticationService.initiateForgotPassword(req),HttpStatus.OK);
	}
	@PostMapping("/verify-otp")
	public ResponseEntity<Object> verifyOtp(@RequestParam int token,@RequestParam String email) {
		return ResponseHandler.successResponse(authenticationService.validatePasswordResetToken(token,email),HttpStatus.OK);
	}
	@PostMapping("/update-password")
	public ResponseEntity<Object> updatePassword(@RequestBody PasswordUpdateRequest req, @RequestParam String email) {
		return ResponseHandler.successResponse(authenticationService.updatePassword(req,email),HttpStatus.OK);
	}

	@PostMapping ("/change-password")
	public ResponseEntity<Object> changePassword(@RequestBody ChangePasswordRequest req, Authentication connectedUser){
		return ResponseHandler.successResponse(authenticationService.changePassword(req,connectedUser),HttpStatus.OK);
	}


}
