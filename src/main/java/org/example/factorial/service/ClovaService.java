package org.example.factorial.service;

import java.util.List;

import org.example.factorial.domain.Article;
import org.example.factorial.domain.Chat;
import org.example.factorial.domain.CompletionExecutor;
import org.example.factorial.repository.ArticleRepository;
import org.example.factorial.repository.ChatRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

@Service
public class ClovaService {

	private final ChatRepository chatRepository;
	private final ArticleRepository articleRepository;

	public ClovaService(ChatRepository chatRepository, ArticleRepository articleRepository) {
		this.chatRepository = chatRepository;
		this.articleRepository = articleRepository;
	}

	public StringBuilder analyzeComment(Long articleId) {
		Article article = articleRepository.findById(articleId)
			.orElseThrow(() -> new NotFoundException("Article not found"));

		List<Chat> allByArticle = chatRepository.findAllByArticle(article);

		if (allByArticle.isEmpty()) {
			return new StringBuilder("댓글이 없습니다.");
		}

		String content = "";
		content += article.getContent() + "\n\n" + allByArticle.toString();
		StringBuilder result = new StringBuilder();
		try {
			CompletionExecutor completionExecutor = new CompletionExecutor("https://clovastudio.stream.ntruss.com",
				"NTA0MjU2MWZlZTcxNDJiY5FLB3VG9qCfw0Z9fm2E090aKH5iLMT8DgRIWmsffPIz",
				"cz58o9rGNInnDnyjuUfYVdoFPJNBcpAKABYKIryl", "536e1a46-3c06-45b6-a7e3-3405a58ce9c7");

			JSONArray presetText = new JSONArray();
			presetText.put(new JSONObject().put("role", "system")
				.put("content",
					"내가 뉴스 기사 요약본을 보내줄거야. 그리고 그 아래에 그 기사에 달린 내용들을 보내줄건데,  그걸 읽고 제일 논리적인 답글 한개를 골라줘야해. 그리고 이 기사 내용에 대한 신뢰성을 기준으로 별로 신뢰할만한 기사가 아니라고 생각되면 1점, 신뢰할만하다 라고 생각하면 10점을 주고, 그 사이 1~10까지 소숫점 첫째짜리까지로 나타내줘."
						+ "무조건 다음과 같은 형태로 응답해야해.\n" +
						"\"답 = 가장 논리적인 유저: ${username}/\n"
						+ "가장 논리적인 채팅: ${chatting}/\n"
						+ "이 기사의 신뢰도 점수: ${ratingAverage}/\n"
						+ "신뢰도 점수 평가 근거: ${reason}\"\n"
						+ "이 형태로 무조건 답변을 주라"
						+ "만약에 논리적인 유저가 없으면 userId 자리에 null을 대신 써줘."
						+ "김용태 국민의힘 의원이 EBS법 개정안에 대한 필리버스터로 13시간 12분을 기록하며 기존 최장 기록을 경신했습니다. \n김 의원은 EBS 프로그램을 열거하며 정치 편향성 여부를 지적했고, 민주당을 향해 이재명 대표를 비판하는 모습을 보이지 않는다고 말했습니다. \n더불어민주당이 주도한 한국교육방송공사법 개정안은 '방송 4법' 중 하나로 이사 수를 21명으로 증원하고, 이사 추천 권한을 다양한 주체로 확대하는 것을 골자로 합니다.\n\n[\n  {\n    \"chatId\": 1,\n    \"text\": \"솔직히 말도 안되는 내용이야\",\n    \"userId\": 1,\n    \"articleId\": 1,\n    \"createdAt\": \"2024-07-29T13:12:34.54912\"\n  },\n  {\n    \"chatId\": 2,\n    \"text\": \"EBS는 정말 재밌는데 김용태 의원이 말도 안되는 내용을 한거네\",\n    \"userId\": 2,\n    \"articleId\": 1,\n    \"createdAt\": \"2024-07-29T13:12:49.613525\"\n  }\n]\n "
						+ "답 : \"가장 논리적인 유저 : {ohw9930}\n"
						+ "가장 논리적인 채팅 : ${EBS는 정말 재밌는데 김용태 의원이 말도 안되는 내용을 한거네}\n"
						+ "이 기사의 신뢰도 점수: ${7.5}.\n"
						+ "신뢰도 점수 평가 근거: ${이는 김용태 의원이 EBS 프로그램의 정치 편향성을 지적한 것에 대해 반박하는 내용으로, EBS 프로그램이 재미있다는 것을 근거로 제시하고 있습니다. 또한, 김용태 의원의 주장이 말도 안된다는 의견을 제시하여, 해당 주장의 타당성에 대해 의문을 제기하고 있습니다. 이러한 점에서, 이 답글은 논리적이고 적절한 내용을 담고 있다고 볼 수 있습니다.}\"\n"));
			presetText.put(new JSONObject().put("role", "user")
				.put("content",
					"김용태 국민의힘 의원이 EBS법 개정안에 대한 필리버스터로 13시간 12분을 기록하며 기존 최장 기록을 경신했습니다. \n김 의원은 EBS 프로그램을 열거하며 정치 편향성 여부를 지적했고, 민주당을 향해 이재명 대표를 비판하는 모습을 보이지 않는다고 말했습니다. \n더불어민주당이 주도한 한국교육방송공사법 개정안은 '방송 4법' 중 하나로 이사 수를 21명으로 증원하고, 이사 추천 권한을 다양한 주체로 확대하는 것을 골자로 합니다.\n\n\n\n\n[\n  {\n    \"chatId\": 1,\n    \"text\": \"솔직히 말도 안되는 내용이야\",\n    \"userId\": 1,\n    \"articleId\": 1,\n    \"createdAt\": \"2024-07-29T13:12:34.54912\"\n  },\n  {\n    \"chatId\": 2,\n    \"text\": \"EBS는 정말 재밌는데 김용태 의원이 말도 안되는 내용을 한거네\",\n    \"userId\": 2,\n    \"articleId\": 1,\n    \"createdAt\": \"2024-07-29T13:12:49.613525\"\n  }\n]"));
			presetText.put(new JSONObject().put("role", "assistant")
				.put("content",
					"답 : \"가장 논리적인 유저 : {ohw9930}\n"
						+ "가장 논리적인 채팅 : ${EBS는 정말 재밌는데 김용태 의원이 말도 안되는 내용을 한거네}\n"
						+ "이 기사의 신뢰도 점수: ${7.5}.\n"
						+ "신뢰도 점수 평가 근거: ${이는 김용태 의원이 EBS 프로그램의 정치 편향성을 지적한 것에 대해 반박하는 내용으로, EBS 프로그램이 재미있다는 것을 근거로 제시하고 있습니다. 또한, 김용태 의원의 주장이 말도 안된다는 의견을 제시하여, 해당 주장의 타당성에 대해 의문을 제기하고 있습니다. 이러한 점에서, 이 답글은 논리적이고 적절한 내용을 담고 있다고 볼 수 있습니다.}\"\n"));
			presetText.put(new JSONObject().put("role", "user").put("content", content));

			JSONObject requestData = new JSONObject();
			requestData.put("messages", presetText);
			requestData.put("topP", 0.6);
			requestData.put("topK", 0);
			requestData.put("maxTokens", 256);
			requestData.put("temperature", 0.1);
			requestData.put("repeatPenalty", 1.2);
			requestData.put("stopBefore", new JSONArray());
			requestData.put("includeAiFilters", true);
			requestData.put("seed", 0);

			result = completionExecutor.execute(requestData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
