package org.example.factorial.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.gson.JsonArray;
import com.nimbusds.jose.shaded.gson.JsonElement;
import com.nimbusds.jose.shaded.gson.JsonParser;

@RestController
public class PythonController {

	@GetMapping("/run-python")
	public String runPythonScript() {
		String result = "";
		try {
			// ProcessBuilder를 사용하여 가상 환경의 Python 스크립트 실행
			ProcessBuilder pb = new ProcessBuilder("venv/bin/python", "news_crawling.py");
			pb.directory(new File("/Users/hyunwoo/Desktop/Factorial/src/main/resources"));
			Process p = pb.start();

			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line + "\n";
			}
			in.close();

			BufferedReader err = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			String errLine;
			while ((errLine = err.readLine()) != null) {
				System.err.println(errLine);
			}
			err.close();

		} catch (Exception e) {
			e.printStackTrace();
			return "Error running Python script";
		}
		return result;
	}

	@GetMapping("/crawl")
	public ResponseEntity<?> crawlNews(@RequestParam String search, @RequestParam int startPage, @RequestParam int endPage) {
		StringBuilder output = new StringBuilder();
		ObjectMapper mapper = new ObjectMapper();
		try {
			ProcessBuilder pb = new ProcessBuilder("venv/bin/python", "news_crawling2.py", search, String.valueOf(startPage), String.valueOf(endPage));
			pb.directory(new File("/Users/hyunwoo/Desktop/Factorial/src/main/resources"));
			Process p = pb.start();
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				output.append(line);
			}
			in.close();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error running Python script");
		}

		try {
			JsonNode jsonNode = mapper.readTree(output.toString());
			return ResponseEntity.ok(jsonNode);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error parsing JSON output");
		}
	}
}
