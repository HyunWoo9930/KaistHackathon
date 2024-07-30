package org.example.factorial.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@Entity
@NoArgsConstructor
public class Inquire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inquireId;
    private LocalDateTime createdAt;

    @Column(columnDefinition = "LONGTEXT")
    private String inquireText;

    @Column(columnDefinition = "LONGTEXT")
    private String answerText;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public Inquire(String inquireText, String answerText, LocalDateTime createdAt){
        this.inquireText = inquireText;
        this.answerText = answerText;
        this.createdAt = createdAt;
    }
}
