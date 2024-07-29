package org.example.factorial.domain.dto.response;

import org.example.factorial.domain.Article;
import org.example.factorial.domain.User;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserRatingHistoryResponse {
	private Long userRatingHistoryId;
	private Long ratingPoint;
	private Long articleId;
	private Long userId;
}
