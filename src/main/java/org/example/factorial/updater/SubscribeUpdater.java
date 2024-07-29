package org.example.factorial.updater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SubscribeUpdater {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 매일 자정에 실행
    @Scheduled(cron = "0 0 0 * * *")
    public void updateExpiredSubscriptions() {
        // 사용자 테이블의 membership 상태를 업데이트하는 쿼리
        String query = "UPDATE user u " +
                "SET u.membership = FALSE " +
                "WHERE u.id IN ( " +
                "    SELECT s.user_id " +
                "    FROM subscribe s " +
                "    WHERE s.end_date <= NOW() " +
                ")";

        jdbcTemplate.update(query);
    }
}