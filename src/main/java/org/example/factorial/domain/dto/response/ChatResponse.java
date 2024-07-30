package org.example.factorial.domain.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ChatResponse {
	private Long chatId;
	private String text;
	private Long userId;
	private Long articleId;
	private LocalDateTime createdAt;
	private String username;
}
