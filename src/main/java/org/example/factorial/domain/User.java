package org.example.factorial.domain;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.*;
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
	private Boolean isActive = true;
	private String account;
	private LocalDateTime createdDate = LocalDateTime.now();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Subscribe> subscriptions;

}
