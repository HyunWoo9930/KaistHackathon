package org.example.factorial.domain.dto.response;

import java.time.LocalDateTime;

import org.example.factorial.domain.Article;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ArticleResponse {
	private Long articleId;
	private String articleDate;
	private LocalDateTime createdAt;
	private String search;
	private String link;
	private String title;
	private String content;
	private Article.ProCon proCon;
	private Long ratingAverage;
	private Long ratingCount;
}
