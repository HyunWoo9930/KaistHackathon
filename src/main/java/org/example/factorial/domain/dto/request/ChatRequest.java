package org.example.factorial.domain.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRequest {
	private String text;
	private Long articleId;
}
