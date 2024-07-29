package org.example.factorial.domain.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompletionRequest {
	private List<Message> messages;
	private double topP;
	private int topK;
	private int maxTokens;
	private double temperature;
	private double repeatPenalty;
	private List<String> stopBefore;
	private boolean includeAiFilters;
	private int seed;

	@Getter
	@Setter
	public static class Message {
		private String role;
		private String content;
	}
}
