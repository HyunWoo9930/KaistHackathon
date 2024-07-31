package org.example.factorial.domain.jwt;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtAuthenticationResponse {
	private String accessToken;
	private String username;
	private String tokenType = "Bearer";

	public JwtAuthenticationResponse(String accessToken, String username) {
		this.accessToken = accessToken;
		this.username = username;
	}
}
