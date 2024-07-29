package org.example.factorial.domain;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.example.factorial.domain.dto.CompletionRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CompletionExecutor {
	private final String host;
	private final String apiKey;
	private final String apiKeyPrimaryVal;
	private final String requestId;

	public CompletionExecutor(String host, String apiKey, String apiKeyPrimaryVal, String requestId) {
		this.host = host;
		this.apiKey = apiKey;
		this.apiKeyPrimaryVal = apiKeyPrimaryVal;
		this.requestId = requestId;
	}

	public String execute(CompletionRequest completionRequest) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		String requestBody = objectMapper.writeValueAsString(completionRequest);

		HttpRequest request = HttpRequest.newBuilder()
			.uri(new URI(host + "/testapp/v1/chat-completions/HCX-003"))
			.header("X-NCP-CLOVASTUDIO-API-KEY", apiKey)
			.header("X-NCP-APIGW-API-KEY", apiKeyPrimaryVal)
			.header("X-NCP-CLOVASTUDIO-REQUEST-ID", requestId)
			.header("Content-Type", "application/json; charset=utf-8")
			.header("Accept", "text/event-stream")
			.POST(HttpRequest.BodyPublishers.ofString(requestBody))
			.build();

		HttpClient client = HttpClient.newHttpClient();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		return response.body();
	}
}
