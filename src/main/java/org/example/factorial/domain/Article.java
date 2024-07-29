package org.example.factorial.domain;

import java.time.LocalDateTime;

import org.example.factorial.Listeners.ArticleListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(ArticleListener.class)
public class Article {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long articleId;
	private String articleDate;
	private LocalDateTime createdAt;
	private String search;
	private String link;
	private String title;
	@Column(columnDefinition = "LONGTEXT")
	private String content;

	public Article(String articleDate, String search, String link, String title, String content) {
		this.articleDate = articleDate;
		this.search = search;
		this.link = link;
		this.title = title;
		this.content = content;
	}
}
