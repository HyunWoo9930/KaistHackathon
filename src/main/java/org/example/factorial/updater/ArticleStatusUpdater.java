package org.example.factorial.updater;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.example.factorial.domain.Article;
import org.example.factorial.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ArticleStatusUpdater {

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Scheduled(cron = "0 0 */6 * * *")
	public void scheduledSaveArticle() {
		String[] searchList = {"정치", "경제", "사회", "문화", "과학", "세계"};
		for (String search : searchList) {
			String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
			int startPage = 1;
			int endPage = search.equals("정치") ? 2 : search.equals("문화") || search.equals("세계") ? 10 : 5;
			Article.ProCon proCon = Article.ProCon.TODAY_NEWS;
			System.out.println("saveArticle start!!!!");
			saveArticle(search, date, startPage, endPage, proCon);
		}
	}

	public void saveArticle(String search, String date, int startPage, int endPage, Article.ProCon proCon) {
		StringBuilder output = new StringBuilder();
		ObjectMapper mapper = new ObjectMapper();
		try {
			ProcessBuilder pb = new ProcessBuilder("/app/venv/bin/python3", "/app/news_crawling2.py", search, date,
				String.valueOf(startPage), String.valueOf(endPage));
			pb.directory(new File("/app"));
			// ProcessBuilder pb = new ProcessBuilder("venv/bin/python", "news_crawling2.py", search, date,
			// 	String.valueOf(startPage), String.valueOf(endPage));
			// pb.directory(new File("/Users/hyunwoo/Desktop/Factorial/src/main/resources"));
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
			if(!output.isEmpty()) {
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
						Article article = new Article(date1, search, link, title, contentText, proCon);
						articleRepository.save(article);
					}
				});
			}
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

	@Scheduled(cron = "0 0 */6 * * *") // 1분마다 실행
	public void updateArticleStatus() {
		String query1 = "UPDATE article SET pro_con = CASE "
			+ "WHEN pro_con = 'TODAY_NEWS' AND rating_count < 200 THEN 'NEUTRAL' "
			+ "WHEN pro_con = 'TODAY_NEWS' AND rating_count >= 200 AND rating_average < 2 THEN 'CON' "
			+ "WHEN pro_con = 'TODAY_NEWS' AND rating_count >= 200 AND rating_average >= 2 AND rating_average < 5 THEN 'PROCON_DECIDING' "
			+ "WHEN pro_con = 'TODAY_NEWS' AND rating_count >= 200 AND rating_average >= 5 THEN 'PRO' "
			+ "ELSE pro_con END "
			+ "WHERE createdAt <= DATE_SUB(NOW(), INTERVAL 1 DAY) AND pro_con = 'TODAY_NEWS';";

		String query2 = "UPDATE article SET pro_con = CASE "
			+ "WHEN pro_con = 'PROCON_DECIDING' AND rating_count < 400 THEN 'NEUTRAL' "
			+ "WHEN pro_con = 'PROCON_DECIDING' AND rating_count >= 400 AND rating_average < 4 THEN 'CON' "
			+ "WHEN pro_con = 'PROCON_DECIDING' AND rating_count >= 400 AND rating_average >= 4 THEN 'PRO' "
			+ "ELSE pro_con END "
			+ "WHERE createdAt <= DATE_SUB(NOW(), INTERVAL 7 DAY) AND pro_con = 'PROCON_DECIDING';";

		jdbcTemplate.update(query1);
		jdbcTemplate.update(query2);
	}
}
