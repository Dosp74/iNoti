package com.example.Notice_reminder.repository;

import com.example.Notice_reminder.entity.EmailRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRecordRepository extends JpaRepository<EmailRecordEntity, Long> {
    boolean existsByUrlAndEmail(String url, String email);
}