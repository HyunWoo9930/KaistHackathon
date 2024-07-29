package org.example.factorial.repository;

import org.example.factorial.domain.UserRatingHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRatingHistoryRepository extends JpaRepository<UserRatingHistory, Long> {
	Boolean existsByUser_UserIdAndArticle_ArticleId(Long userId, Long articleId);
}
