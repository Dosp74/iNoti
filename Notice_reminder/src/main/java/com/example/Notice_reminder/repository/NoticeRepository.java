package com.example.Notice_reminder.repository;

import com.example.Notice_reminder.entity.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<NoticeEntity, Long> {
}