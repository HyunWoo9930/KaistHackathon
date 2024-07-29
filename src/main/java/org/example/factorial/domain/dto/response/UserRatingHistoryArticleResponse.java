package org.example.factorial.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserRatingHistoryArticleResponse {
	private Long userRatingHistoryId;
	private Long ratingPoint;
	private Long userId;
}
