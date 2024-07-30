package org.example.factorial.controller;

import org.example.factorial.domain.Inquire;
import org.example.factorial.domain.dto.response.InquireAnswerResponse;
import org.example.factorial.domain.dto.response.InquireResponse;
import org.example.factorial.service.InquireService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/Inquire")
@CrossOrigin(origins = "*")
public class InquireController {
    private final InquireService inquireService;

    public InquireController(InquireService inquireService){
        this.inquireService = inquireService;
    }

    @PostMapping("/question")
    public ResponseEntity<?> saveQuestion(
            @RequestParam("inquireText") String inquireText,
            @AuthenticationPrincipal UserDetails userDetails
    ){
        try{
            InquireResponse response = inquireService.saveQuestion(userDetails, inquireText);
            return ResponseEntity.ok(response);
        }catch(NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/answer")
    public ResponseEntity<?> saveResponse(
            @RequestParam("answerText") String answerText,
            @RequestParam("inquireId") Long inquireId,
            @AuthenticationPrincipal UserDetails userDetails
    ){
        try{
            InquireAnswerResponse response = inquireService.saveResponse(userDetails, answerText, inquireId);
            return ResponseEntity.ok(response);
        }catch(NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllNotice(){
        List<InquireAnswerResponse> responses = inquireService.getAllResponses();
        return ResponseEntity.ok(responses);
    }
}
