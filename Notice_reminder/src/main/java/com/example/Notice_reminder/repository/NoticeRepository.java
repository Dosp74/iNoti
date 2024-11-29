package com.example.Notice_reminder.repository;

import com.example.Notice_reminder.entity.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<NoticeEntity, Long> {
    // 키워드가 제목에 포함된 공지사항을 찾음
    List<NoticeEntity> findByTitleContaining(String keyword);
}
