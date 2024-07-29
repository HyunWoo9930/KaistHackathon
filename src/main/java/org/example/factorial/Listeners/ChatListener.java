package org.example.factorial.Listeners;

import java.time.LocalDateTime;

import org.example.factorial.domain.Article;
import org.example.factorial.domain.Chat;

import jakarta.persistence.PrePersist;

public class ChatListener {
	@PrePersist
	public void prePersist(Chat chat) {
		LocalDateTime now = LocalDateTime.now();
		chat.setCreatedAt(now);
	}
}
