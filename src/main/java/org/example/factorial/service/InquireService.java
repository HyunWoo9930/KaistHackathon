package org.example.factorial.service;

import org.example.factorial.domain.Inquire;
import org.example.factorial.domain.User;
import org.example.factorial.domain.dto.response.InquireAnswerResponse;
import org.example.factorial.domain.dto.response.InquireResponse;
import org.example.factorial.domain.dto.response.UserResponse;
import org.example.factorial.repository.InquireRepository;
import org.example.factorial.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class InquireService {

    private final InquireRepository inquireRepository;
    private final UserRepository userRepository;

    public InquireService(InquireRepository inquireRepository, UserRepository userRepository){
        this.inquireRepository = inquireRepository;
        this.userRepository = userRepository;
    }

    public InquireResponse saveQuestion(UserDetails userDetails, String inquireText){
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Inquire inquire = new Inquire();
        inquire.setInquireText(inquireText);
        inquire.setCreatedAt(LocalDateTime.now());
        inquire.setUser(user);

        Inquire save = inquireRepository.save(inquire);

        return new InquireResponse(save.getInquireId(),save.getInquireText());
    }

    public InquireAnswerResponse saveResponse(UserDetails userDetails, String answerText, Long inquireId) throws AccessDeniedException {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Inquire inquire = inquireRepository.findById(inquireId)
                .orElseThrow(() -> new NotFoundException("Question not found"));

        if(user.getUsername().equals("admin")){
            throw new AccessDeniedException("답변 권한이 없습니다.");
        }

        inquire.setAnswerText(answerText);
        Inquire save = inquireRepository.save(inquire);

        return new InquireAnswerResponse(save.getInquireText(), save.getAnswerText(),save.getCreatedAt());
    }

    public List<InquireAnswerResponse> getAllResponses(){
        return inquireRepository.findAll().stream().map(inquire -> new InquireAnswerResponse(
                inquire.getAnswerText(), inquire.getInquireText(), inquire.getCreatedAt()
        )).toList();
    }
}
