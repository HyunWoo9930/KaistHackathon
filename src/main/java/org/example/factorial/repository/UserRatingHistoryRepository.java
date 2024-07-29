package org.example.factorial.repository;

import java.util.List;

import org.example.factorial.domain.UserRatingHistory;
import org.example.factorial.domain.dto.response.UserRatingHistoryResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRatingHistoryRepository extends JpaRepository<UserRatingHistory, Long> {
	Boolean existsByUser_UserIdAndArticle_ArticleId(Long userId, Long articleId);
	List<UserRatingHistory>  findAllByUser_UserId(Long userId);
	List<UserRatingHistory>  findAllByArticle_ArticleId(Long articleId);
}
