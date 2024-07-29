package org.example.factorial.domain.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResponse {
	private Long id;
	private String username;
	private String password;
	private String email;
	private Boolean membership;

	public UserResponse(Long id, String username, String password, String email, Boolean membership) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.membership = membership;
	}
}
