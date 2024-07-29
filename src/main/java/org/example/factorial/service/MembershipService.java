package org.example.factorial.service;

import jakarta.validation.constraints.Null;
import org.example.factorial.domain.Subscribe;
import org.example.factorial.domain.User;
import org.example.factorial.repository.SubscribeRepository;
import org.example.factorial.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;

@Service
public class MembershipService {

    private final UserRepository userRepository;
    private final SubscribeRepository subscribeRepository;

    public MembershipService(UserRepository userRepository, SubscribeRepository subscribeRepository){
        this.userRepository = userRepository;
        this.subscribeRepository = subscribeRepository;
    }



    public void joinMembership(UserDetails userDetails, String account) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (user.getMembership()) {
            throw new RuntimeException("이미 구독중입니다.");
        }

        if ((user.getAccount() == null) || (user.getAccount() == account)) {
            user.setAccount(account);
            user.setMembership(true);
            userRepository.save(user);

            Subscribe sub = new Subscribe();
            sub.setId(user.getUserId());
            sub.setStartDate(LocalDateTime.now());
            sub.setEndDate(sub.getStartDate().plusMinutes(3));
            subscribeRepository.save(sub);

        } else {
            throw new RuntimeException("잘못된 계좌입니다.");
        }
    }

    public void freeMembership(UserDetails userDetails){
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if(!user.getIsActive()){
            throw new RuntimeException("이미 무료 체험을 진행하였습니다.");
        }

        if(user.getMembership()){
            throw new RuntimeException("이미 구독중입니다.");
        }

        user.setMembership(true);
        user.setIsActive(false);
        userRepository.save(user);

        Subscribe sub = new Subscribe();
        sub.setId(user.getUserId());
        sub.setStartDate(LocalDateTime.now());
        sub.setEndDate(sub.getStartDate().plusDays(7));
        subscribeRepository.save(sub);
    }
}
