package org.example.factorial.service;

import java.util.Optional;

import org.example.factorial.domain.User;
import org.example.factorial.domain.dto.response.UserResponse;
import org.example.factorial.domain.jwt.JwtAuthenticationResponse;
import org.example.factorial.domain.jwt.JwtTokenProvider;
import org.example.factorial.domain.signinup.LoginRequest;
import org.example.factorial.domain.signinup.SignUpRequest;
import org.example.factorial.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import jakarta.transaction.Transactional;

@Service
public class AuthService {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	JwtTokenProvider tokenProvider;

	@Autowired
	UserRepository userRepository;

	public JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(
				loginRequest.getUsername(),
				loginRequest.getPassword()
			)
		);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = tokenProvider.generateToken(authentication.getName());
		Optional<User> byUserId = userRepository.findByUsername(loginRequest.getUsername());
		if (byUserId.isPresent()) {
			return new JwtAuthenticationResponse(jwt);
		} else {
			throw new NotFoundException("User Not Found");
		}
	}

	@Transactional
	public UserResponse register(SignUpRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUserId())) {
			throw new RuntimeException("Username is already taken!");
		}

		User user = new User();
		user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
		user.setEmail(signUpRequest.getEmail());
		user.setUsername(signUpRequest.getUserId());
		user.setName(signUpRequest.getName());

		User save = userRepository.save(user);
		return new UserResponse(save.getId(), save.getUsername(), save.getPassword(), save.getEmail(),
			save.getMembership());
	}

	public void changePassword(UserDetails userDetails, String password) {
		userRepository.findByUsername(userDetails.getUsername()).ifPresent(user -> {
			user.setPassword(passwordEncoder.encode(password));
			userRepository.save(user);
		});
	}
}
