package org.example.factorial.repository;

import java.util.List;

import org.example.factorial.domain.Article;
import org.example.factorial.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
	List<Chat>  findAllByArticle(Article article);
}
