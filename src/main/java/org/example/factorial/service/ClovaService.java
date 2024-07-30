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
					"내가 뉴스 기사 요약본을 보내줄거야. 그리고 그 아래에 그 기사에 달린 내용들을 보내줄건데,  그걸 읽고 제일 논리적인 답글 한개를 골라줘야해. 하지만, 너가 생각했을때 논리적인 댓글이 없으면 답 = 없음 하고 없는 이유를 적어줘. 그리고 이 기사 내용에 대한 신뢰성을 기준으로 별로 신뢰할만한 기사가 아니라고 생각되면 1점, 신뢰할만하다 라고 생각하면 10점을 주고, 그 사이 1~10까지 소숫점 첫째짜리까지로 나타내줘.\n\n김용태 국민의힘 의원이 EBS법 개정안에 대한 필리버스터로 13시간 12분을 기록하며 기존 최장 기록을 경신했습니다. \n김 의원은 EBS 프로그램을 열거하며 정치 편향성 여부를 지적했고, 민주당을 향해 이재명 대표를 비판하는 모습을 보이지 않는다고 말했습니다. \n더불어민주당이 주도한 한국교육방송공사법 개정안은 '방송 4법' 중 하나로 이사 수를 21명으로 증원하고, 이사 추천 권한을 다양한 주체로 확대하는 것을 골자로 합니다.\n\n[\n  {\n    \"chatId\": 1,\n    \"text\": \"솔직히 말도 안되는 내용이야\",\n    \"userId\": 1,\n    \"articleId\": 1,\n    \"createdAt\": \"2024-07-29T13:12:34.54912\"\n  },\n  {\n    \"chatId\": 2,\n    \"text\": \"EBS는 정말 재밌는데 김용태 의원이 말도 안되는 내용을 한거네\",\n    \"userId\": 2,\n    \"articleId\": 1,\n    \"createdAt\": \"2024-07-29T13:12:49.613525\"\n  }\n]\n답 : chatId = 2, 2인 이유\n\n"));
			presetText.put(new JSONObject().put("role", "user")
				.put("content",
					"김용태 국민의힘 의원이 EBS법 개정안에 대한 필리버스터로 13시간 12분을 기록하며 기존 최장 기록을 경신했습니다. \n김 의원은 EBS 프로그램을 열거하며 정치 편향성 여부를 지적했고, 민주당을 향해 이재명 대표를 비판하는 모습을 보이지 않는다고 말했습니다. \n더불어민주당이 주도한 한국교육방송공사법 개정안은 '방송 4법' 중 하나로 이사 수를 21명으로 증원하고, 이사 추천 권한을 다양한 주체로 확대하는 것을 골자로 합니다.\n\n\n\n\n[\n  {\n    \"chatId\": 1,\n    \"text\": \"솔직히 말도 안되는 내용이야\",\n    \"userId\": 1,\n    \"articleId\": 1,\n    \"createdAt\": \"2024-07-29T13:12:34.54912\"\n  },\n  {\n    \"chatId\": 2,\n    \"text\": \"EBS는 정말 재밌는데 김용태 의원이 말도 안되는 내용을 한거네\",\n    \"userId\": 2,\n    \"articleId\": 1,\n    \"createdAt\": \"2024-07-29T13:12:49.613525\"\n  }\n]"));
			presetText.put(new JSONObject().put("role", "assistant")
				.put("content",
					"답 : chatId = 2\n\n해당 기사에 대한 논리적인 답글은 chatId가 2인 \"EBS는 정말 재밌는데 김용태 의원이 말도 안되는 내용을 한거네\" 입니다. 이는 김용태 의원이 EBS 프로그램의 정치 편향성을 지적한 것에 대해 반박하는 내용으로, EBS 프로그램이 재미있다는 것을 근거로 제시하고 있습니다. 또한, 김용태 의원의 주장이 말도 안된다는 의견을 제시하여, 해당 주장의 타당성에 대해 의문을 제기하고 있습니다. 이러한 점에서, 이 답글은 논리적이고 적절한 내용을 담고 있다고 볼 수 있습니다."));
			presetText.put(new JSONObject().put("role", "user")
				.put("content",
					"더불어민주당 '검찰독재 정치탄압 대책위원회'는 이재명 전 대표를 쌍방울그룹 대북송금 사건 연루 혐의로 불구속 기소한 수원지검 형사6부 서현욱 부장검사를 공수처에 고발했습니다. \n\n\n대책위는 국회 기자회견에서 이 전 대표의 혐의가 담긴 검찰 작성 공소장이 허위라고 주장했으며, 서 검사는 이재명이 마치 '쌍방울그룹이 경기도의 스마트팜 비용 대납을 알고 있던 것'처럼 꾸며내 거짓 삼류소설을 완성한 것이라 했습니다.  \n\n\n또한 \"정치검찰의 사건 조작 행태가 거짓으로 조작된 공소장으로 이어졌다\"며 \"공소장에서 찾아볼 수 있는 증거라고는 본인들(검찰)의 입맛에 맞게 조작된 허위 진술 뿐\"이라며 모든 과정은 결국 이 전 대표를 겨냥한 정치적 공작의 일환이라고 밝혔습니다. \n대북송금 사건에서 이 전 대표와 공범 관계를 의심받고 있는 이화영 전 경기부지사는 지난 6월 1심에서 징역 9년6개월을 선고받았습니다.\n\n\n\n\n\n\n\n\n[\n  {\n    \"chatId\": 1,\n    \"text\": \"솔직히 말도 안되는 내용이야\",\n    \"userId\": 1,\n    \"articleId\": 1,\n    \"createdAt\": \"2024-07-29T13:12:34.54912\"\n  },\n  {\n    \"chatId\": 2,\n    \"text\": \"EBS는 정말 재밌는데 김용태 의원이 말도 안되는 내용을 한거네\",\n    \"userId\": 2,\n    \"articleId\": 1,\n    \"createdAt\": \"2024-07-29T13:12:49.613525\"\n  }\n]"));
			presetText.put(new JSONObject().put("role", "assistant")
				.put("content",
					"답 : chatId = 1\n\n해당 기사에 대한 논리적인 답글은 chatId가 1인 \"솔직히 말도 안되는 내용이야\" 입니다. 이는 대책위가 이재명 전 대표의 혐의가 담긴 검찰 작성 공소장이 허위라고 주장한 것에 대해 동의하는 내용으로, 검찰의 사건 조작 행태와 허위 진술을 비판하고 있습니다. 또한, 이 전 대표를 겨냥한 정치적 공작의 일환이라고 주장하며, 검찰의 수사가 불공정하다는 의견을 제시하고 있습니다. 이러한 점에서, 이 답글은 논리적이고 적절한 내용을 담고 있다고 볼 수 있습니다."));
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
