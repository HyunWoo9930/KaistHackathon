package org.example.factorial.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
	private String username;
	private String name;
	private String password;
	private String email;
	private Boolean membership = false;
	private LocalDateTime createdDate = LocalDateTime.now();
	private Boolean isSubscribed;
}
