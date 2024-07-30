package org.example.factorial.domain.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class InquireAnswerResponse {
    private String inquireText;
    private String answerText;
    private LocalDateTime createdAt;

    public InquireAnswerResponse(String inquireText, String answerText, LocalDateTime createdAt){
        this.inquireText = inquireText;
        this.answerText = answerText;
        this.createdAt = createdAt;
    }
}
