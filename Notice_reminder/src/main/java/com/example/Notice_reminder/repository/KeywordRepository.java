package com.example.Notice_reminder.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Notice_reminder.entity.KeywordEntity;

@Repository
public interface KeywordRepository extends JpaRepository<KeywordEntity, Long> {
}


