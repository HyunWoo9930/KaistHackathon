package org.example.factorial.domain.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InquireResponse {
    private Long inquireId;
    private String inquireText;

    public InquireResponse(Long inquireId, String inquireText){
        this.inquireId = inquireId;
        this.inquireText = inquireText;
    }
}
