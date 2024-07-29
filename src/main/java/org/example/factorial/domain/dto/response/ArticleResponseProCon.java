package org.example.factorial.domain.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ArticleResponseProCon {
	private List<ArticleResponse> proArticle;
	private List<ArticleResponse> conArticle;
}
