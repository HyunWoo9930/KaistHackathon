package org.example.factorial.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.example.factorial.domain.Article;
import org.example.factorial.domain.UserRatingHistory;
import org.example.factorial.domain.dto.request.UserRatingHistoryRequest;
import org.example.factorial.domain.dto.response.ArticleResponse;
import org.example.factorial.domain.dto.response.UserRatingHistoryResponse;
import org.example.factorial.service.ArticleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.webjars.NotFoundException;

import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/api/article")
@CrossOrigin(origins = "*")
public class ArticleController {

	private final ArticleService articleService;

	public ArticleController(ArticleService articleService) {
		this.articleService = articleService;
	}

	@GetMapping("")
	public ResponseEntity<?> getArticle(@RequestParam String search, @RequestParam(required = false) String date,
		@RequestParam int startPage, @RequestParam int endPage) {
		if (date == null || date.isEmpty()) {
			date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
		}
		try {
			List<Article> articles = articleService.getArticle(search, date, startPage, endPage);
			return ResponseEntity.ok(articles);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@PostMapping("/rating_trust_level")
	public ResponseEntity<?> postRatingTrustLevel(@AuthenticationPrincipal UserDetails userDetails,
		@RequestBody UserRatingHistoryRequest userRatingHistoryRequest) {
		try {
			UserRatingHistoryResponse response = articleService.rateArticle(userDetails, userRatingHistoryRequest);
			return ResponseEntity.ok(response);
		} catch (NotFoundException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@GetMapping("{article_id}")
	public ResponseEntity<?> getArticle(@PathVariable Long article_id) {
		try {
			ArticleResponse article = articleService.getArticle(article_id);
			return ResponseEntity.ok(article);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
}
