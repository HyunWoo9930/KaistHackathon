package org.example.factorial.controller;

import org.example.factorial.domain.dto.response.UserResponse;
import org.example.factorial.domain.signinup.LoginRequest;
import org.example.factorial.domain.signinup.SignUpRequest;
import org.example.factorial.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.webjars.NotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

	@Autowired
	AuthService authService;

	@PostMapping("/sign_in")
	@Operation(summary = "로그인")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		try {
			return ResponseEntity.ok(authService.authenticateUser(loginRequest));
		} catch (NotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@PostMapping("/sign_up")
	@Operation(summary = "회원가입")
	public ResponseEntity<?> register(
		@Valid @RequestBody SignUpRequest signUpRequest
	) {
		try {
			UserResponse register = authService.register(signUpRequest);
			return ResponseEntity.ok(register);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@PutMapping("/change_pw")
	@Operation(summary = "비밀번호 변경")
	public ResponseEntity<?> changePassword(
		@AuthenticationPrincipal UserDetails userDetails,
		@RequestParam(value = "new_pw") String newPassword) {
		try {
			authService.changePassword(userDetails, newPassword);
			return ResponseEntity.ok("Password changed successfully");
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
}
