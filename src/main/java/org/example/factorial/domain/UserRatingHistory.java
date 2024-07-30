package org.example.factorial.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class UserRatingHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userRatingHistoryId;
	private Long ratingPoint;
	private LocalDate ratingDate;

	@ManyToOne
	@JoinColumn(name = "article_id")
	private Article article;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public UserRatingHistory(Long ratingPoint, Article article, User user, LocalDate ratingDate) {
		this.ratingPoint = ratingPoint;
		this.article = article;
		this.user = user;
		this.ratingDate = ratingDate;
	}

	public UserRatingHistory() {

	}
}
