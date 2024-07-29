package org.example.factorial.controller;

import java.util.List;

import org.example.factorial.domain.dto.request.ChatRequest;
import org.example.factorial.domain.dto.response.ChatResponse;
import org.example.factorial.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.webjars.NotFoundException;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

	private final ChatService chatService;

	public ChatController(ChatService chatService) {
		this.chatService = chatService;
	}

	@PostMapping("/chatting_post")
	public ResponseEntity<?> postChat(
		@AuthenticationPrincipal UserDetails userDetails,
		@RequestBody ChatRequest chatRequest
	) {
		try {
			ChatResponse chatResponse = chatService.postChat(userDetails, chatRequest);
			return ResponseEntity.ok(chatResponse);
		} catch (NotFoundException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/chatting_get")
	public ResponseEntity<?> getChat(
		@RequestParam(value = "articleId") Long articleId
	) {
		try {
			List<ChatResponse> chat = chatService.getChat(articleId);
			return ResponseEntity.ok(chat);
		} catch (NotFoundException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
