package org.example.factorial.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.example.factorial.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
	List<Article> findArticlesByArticleDateAndSearch(String date, String search);
}
