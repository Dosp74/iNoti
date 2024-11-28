package com.example.Notice_reminder.repository;

import com.example.Notice_reminder.entity.KeywordEntity;
import com.example.Notice_reminder.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KeywordRepository extends JpaRepository<KeywordEntity, Long> {
    // 특정 사용자가 등록한 모든 키워드 조회
    List<KeywordEntity> findByMember(MemberEntity member);

    // 특정 사용자가 등록한 특정 키워드 조회
    Optional<KeywordEntity> findByKeywordAndMember(String keyword, MemberEntity member);
}





