package com.example.Notice_reminder.repository;

import com.example.Notice_reminder.entity.KeywordEntity;
import com.example.Notice_reminder.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KeywordRepository extends JpaRepository<KeywordEntity, Long> {
    List<KeywordEntity> findByMember(MemberEntity member);
}




