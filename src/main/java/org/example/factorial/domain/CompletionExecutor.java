package org.example.factorial.domain;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

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

	public StringBuilder execute(JSONObject completionRequest) throws Exception {
		URL url = new URL(this.host + "/testapp/v1/chat-completions/HCX-003");
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("X-NCP-CLOVASTUDIO-API-KEY", this.apiKey);
		conn.setRequestProperty("X-NCP-APIGW-API-KEY", this.apiKeyPrimaryVal);
		conn.setRequestProperty("X-NCP-CLOVASTUDIO-REQUEST-ID", this.requestId);
		conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
		conn.setRequestProperty("Accept", "text/event-stream");
		conn.setDoOutput(true);

		try (OutputStream os = conn.getOutputStream()) {
			byte[] input = completionRequest.toString().getBytes("utf-8");
			os.write(input, 0, input.length);
		}

		StringBuilder result = null;
		boolean captureNextLine = false;

		try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
			String responseLine;
			while ((responseLine = br.readLine()) != null && result == null) {
				System.out.println("responseLine = " + responseLine); // Debugging output
				if (captureNextLine) {
					String data = responseLine.substring(5).trim();
					System.out.println("data = " + data);
					result = new StringBuilder((data));
				}
				if (responseLine.startsWith("event:result")) {
					captureNextLine = true;
				}
			}
		}
		return result;
	}
}
