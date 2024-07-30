package org.example.factorial.repository;

import org.example.factorial.domain.Inquire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InquireRepository extends JpaRepository<Inquire,Long> {
}
