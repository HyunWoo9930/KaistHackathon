package org.example.factorial.Listeners;

import java.time.LocalDateTime;

import org.example.factorial.domain.Article;

import jakarta.persistence.PrePersist;

public class ArticleListener {
	@PrePersist
	public void prePersist(Article article) {
		LocalDateTime now = LocalDateTime.now();
		article.setCreatedAt(now);
	}
}
