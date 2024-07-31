package org.example.factorial.domain.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class InquireAnswerResponse {
    private Long inquireId;
    private String inquireText;
    private String answerText;
    private LocalDateTime createdAt;

    public InquireAnswerResponse(Long inquireId, String inquireText, String answerText, LocalDateTime createdAt) {
        this.inquireId = inquireId;
        this.inquireText = inquireText;
        this.answerText = answerText;
        this.createdAt = createdAt;
    }
}
