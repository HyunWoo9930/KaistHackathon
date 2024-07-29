package org.example.factorial.domain;

import java.time.LocalDateTime;

import org.example.factorial.Listeners.ArticleListener;
import org.example.factorial.Listeners.ChatListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@EntityListeners(ChatListener.class)
public class Chat {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long chatId;

	@ManyToOne
	@JoinColumn(name = "article_id")
	private Article article;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	private String text;
	private LocalDateTime createdAt;

	public Chat(Article article, User user, String text) {
		this.article = article;
		this.user = user;
		this.text = text;
	}

	public Chat() {

	}
}
