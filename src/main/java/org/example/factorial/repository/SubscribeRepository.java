package org.example.factorial.repository;


import jakarta.transaction.Transactional;
import org.example.factorial.domain.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {
	List<Subscribe> findAllByEndDateBefore(LocalDateTime now);
}
