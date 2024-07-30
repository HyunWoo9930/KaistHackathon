package org.example.factorial.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class UserRatingHistoryInfoResponse {
	private Long userRatingHistoryId;
	private Long ratingPoint;
	private LocalDate ratingDate;
	private String newsLink;
}
