package org.example.factorial.controller;

import org.example.factorial.service.ClovaService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class ClovaController {
	private final ClovaService clovaService;

	public ClovaController(ClovaService clovaService) {
		this.clovaService = clovaService;
	}

	@GetMapping("/analyzeComment")
	public ResponseEntity<?> analyzeComment(
		@RequestParam(value = "articleId") Long articleId
	) {
		StringBuilder test = clovaService.analyzeComment(articleId);
		return ResponseEntity.ok(test);
	}
}
