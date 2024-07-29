package org.example.factorial.service;

import java.time.LocalDateTime;
import java.util.List;

import org.example.factorial.domain.Subscribe;
import org.example.factorial.domain.User;
import org.example.factorial.repository.SubscribeRepository;
import org.example.factorial.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

@Service
public class MembershipService {

	private final UserRepository userRepository;
	private final SubscribeRepository subscribeRepository;

	public MembershipService(UserRepository userRepository, SubscribeRepository subscribeRepository) {
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
			sub.setUser(user);
			sub.setStartDate(LocalDateTime.now());
			sub.setEndDate(sub.getStartDate().plusDays(30));
			subscribeRepository.save(sub);

		} else {
			throw new RuntimeException("잘못된 계좌입니다.");
		}
	}

	public void freeMembership(UserDetails userDetails) {
		User user = userRepository.findByUsername(userDetails.getUsername())
			.orElseThrow(() -> new NotFoundException("User not found"));

		if (!user.getIsActive()) {
			throw new RuntimeException("이미 무료 체험을 진행하였습니다.");
		}

		if (user.getMembership()) {
			throw new RuntimeException("이미 구독중입니다.");
		}

		user.setMembership(true);
		user.setIsActive(false);
		userRepository.save(user);

		Subscribe sub = new Subscribe();
		sub.setUser(user);
		sub.setStartDate(LocalDateTime.now());
		sub.setEndDate(sub.getStartDate().plusDays(7));
		subscribeRepository.save(sub);
	}

	@Transactional
	@Scheduled(cron = "0 */1 * * * *")
	public void updateExpiredSubscriptions() {
		LocalDateTime now = LocalDateTime.now();
		List<Subscribe> expiredSubscriptions = subscribeRepository.findAllByEndDateBefore(now);

		expiredSubscriptions.forEach(subscribe -> {
			subscribeRepository.delete(subscribe);
			User user = subscribe.getUser();
			if (user != null && user.getMembership() != null && user.getMembership()) {
				user.setMembership(false);
				userRepository.save(user);
			}
		});
	}
}
