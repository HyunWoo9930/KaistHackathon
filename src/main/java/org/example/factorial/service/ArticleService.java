package org.example.factorial.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.example.factorial.domain.Article;
import org.example.factorial.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ArticleService {

	@Autowired
	private RestTemplate restTemplate;

	private final ArticleRepository articleRepository;

	public ArticleService(ArticleRepository articleRepository) {
		this.articleRepository = articleRepository;
	}

	public List<Article> getArticle(String search, String date, int startPage, int endPage) {
		String newDate = date.split("\\.")[0] + "-" + date.split("\\.")[1] + "-" + date.split("\\.")[2];
		List<Article> articles = articleRepository.findArticlesByArticleDateAndSearch(newDate, search);

		if (articles.isEmpty()) {
			saveArticle(search, date, startPage, endPage);
			articles = articleRepository.findArticlesByArticleDateAndSearch(newDate, search);
		}
		return articles;
	}

	public void saveArticle(String search, String date, int startPage, int endPage) {
		StringBuilder output = new StringBuilder();
		ObjectMapper mapper = new ObjectMapper();
		try {
			ProcessBuilder pb = new ProcessBuilder("venv/bin/python", "news_crawling2.py", search, date,
				String.valueOf(startPage), String.valueOf(endPage));
			pb.directory(new File("/Users/hyunwoo/Desktop/Factorial/src/main/resources"));
			Process p = pb.start();
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				output.append(line);
			}
			in.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		try {
			JsonNode jsonNode = mapper.readTree(output.toString());
			jsonNode.forEach(json -> {
				String dates = json.get("date").asText();
				String title = json.get("title").asText();
				String link = json.get("link").asText();
				String contentText = null;
				try {
					String contentJson = sendRequest(json.get("content").toString());
					JsonNode content = mapper.readTree(contentJson);
					JsonNode messageNode = content.get("message");
					if (messageNode != null) {
						JsonNode contentNode = messageNode.get("content");
						if (contentNode != null) {
							contentText = contentNode.asText();
						}
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				if (contentText != null) {
					String date1 = dates.split(" ")[0];
					Article article = new Article(date1, search, link, title, contentText);
					articleRepository.save(article);
				}
			});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String sendRequest(String content) {
		String urlString = "https://clovastudio.stream.ntruss.com/testapp/v1/chat-completions/HCX-003";

		try {
			HttpURLConnection connection = getHttpURLConnection(urlString);

			String jsonPayload = "{\n" +
				"  \"messages\": [\n" +
				"    {\n" +
				"      \"role\": \"system\",\n" +
				"      \"content\": \"내가 네이버 기사 내용을 주는데,\\n10줄 이내로 요약해줘.\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"role\": \"user\",\n" +
				"      \"content\": " + content + "\n" +
				"    }\n" +
				"  ],\n" +
				"  \"topP\": 0.8,\n" +
				"  \"topK\": 0,\n" +
				"  \"maxTokens\": 256,\n" +
				"  \"temperature\": 0.5,\n" +
				"  \"repeatPenalty\": 5.0,\n" +
				"  \"stopBefore\": [],\n" +
				"  \"includeAiFilters\": true,\n" +
				"  \"seed\": 0\n" +
				"}";

			connection.getOutputStream().write(jsonPayload.getBytes());

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			StringBuilder result = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("event:result")) {
					line = reader.readLine();
					if (line != null && line.startsWith("data:")) {
						result.append(line.substring(5)).append("\n");
					}
				}
			}
			reader.close();
			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static HttpURLConnection getHttpURLConnection(String urlString) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("X-NCP-CLOVASTUDIO-API-KEY",
			"NTA0MjU2MWZlZTcxNDJiY5FLB3VG9qCfw0Z9fm2E090aKH5iLMT8DgRIWmsffPIz");
		connection.setRequestProperty("X-NCP-APIGW-API-KEY", "cz58o9rGNInnDnyjuUfYVdoFPJNBcpAKABYKIryl");
		connection.setRequestProperty("X-NCP-CLOVASTUDIO-REQUEST-ID", "db4e9620-7932-4a66-bb3d-191e333a821a");
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("Accept", "text/event-stream");
		connection.setDoOutput(true);
		return connection;
	}
}
