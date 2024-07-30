package org.example.factorial.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UserResponse {
	private Long id;
	private String username;
	private String password;
	private String email;
	private Boolean membership;
	private String name;
	private String account;
}
