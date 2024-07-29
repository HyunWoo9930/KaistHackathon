package org.example.factorial.domain.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRatingHistoryRequest {
	private Long rate;
	private Long articleId;
}
