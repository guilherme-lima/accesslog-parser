package com.ef.repository;

import com.ef.domain.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by guilherme-lima on 15/07/18.
 * https://github.com/guilherme-lima
 */
@Repository
public interface AccessLogRepository extends JpaRepository<AccessLog, LocalDateTime> {

    List<AccessLog> findAllByDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}