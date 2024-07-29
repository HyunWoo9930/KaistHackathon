package org.example.factorial.domain.signinup;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {

	@NotBlank
	@Size(min = 3, max = 50)
	private String userId;
	@NotBlank
	private String name;

	@NotBlank
	@Size(min = 6, max = 100)
	private String password;

	@NotBlank
	@Email(message = "Email should be valid")
	@Size(max = 100)
	private String email;
}
