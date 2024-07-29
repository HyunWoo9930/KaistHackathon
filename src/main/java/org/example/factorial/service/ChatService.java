package org.example.factorial.service;

import java.util.List;

import org.example.factorial.domain.Article;
import org.example.factorial.domain.Chat;
import org.example.factorial.domain.User;
import org.example.factorial.domain.dto.request.ChatRequest;
import org.example.factorial.domain.dto.response.ChatResponse;
import org.example.factorial.repository.ArticleRepository;
import org.example.factorial.repository.ChatRepository;
import org.example.factorial.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

@Service
public class ChatService {
	private final ChatRepository chatRepository;
	private final ArticleRepository articleRepository;
	private final UserRepository userRepository;

	public ChatService(ChatRepository chatRepository, ArticleRepository articleRepository,
		UserRepository userRepository) {
		this.chatRepository = chatRepository;
		this.articleRepository = articleRepository;
		this.userRepository = userRepository;
	}

	public ChatResponse postChat(UserDetails userDetails, ChatRequest chatRequest) {
		Article article = articleRepository.findById(chatRequest.getArticleId())
			.orElseThrow(() -> new NotFoundException("Article not found"));
		User user = userRepository.findByUsername(userDetails.getUsername())
			.orElseThrow(() -> new NotFoundException("User not found"));

		Chat chat = new Chat(article, user, chatRequest.getText());
		Chat save = chatRepository.save(chat);

		return new ChatResponse(save.getChatId(), save.getText(), save.getUser().getUserId(),
			save.getArticle().getArticleId(), save.getCreatedAt());
	}

	public List<ChatResponse> getChat(Long articleId) {
		Article article = articleRepository.findById(articleId)
			.orElseThrow(() -> new NotFoundException("Article not found"));

		List<Chat> chats = chatRepository.findAllByArticle(article);
		return chats.stream()
			.map(chat -> new ChatResponse(chat.getChatId(), chat.getText(), chat.getUser().getUserId(),
				chat.getArticle().getArticleId(), chat.getCreatedAt()))
			.toList();
	}
}
