package com.example.Notice_reminder.service;

import org.springframework.stereotype.Service;
import com.example.Notice_reminder.entity.KeywordEntity;
import com.example.Notice_reminder.entity.MemberEntity;
import com.example.Notice_reminder.repository.KeywordRepository;
import com.example.Notice_reminder.repository.MemberRepository;

import java.time.LocalDateTime;

@Service
public class KeywordService {

    private final KeywordRepository keywordRepository;
    private final MemberRepository memberRepository;

    public KeywordService(KeywordRepository keywordRepository, MemberRepository memberRepository) {
        this.keywordRepository = keywordRepository;
        this.memberRepository = memberRepository;
    }

    public void saveKeyword(String keyword, String memberEmail) {
        // 이메일로 회원 정보 조회
        MemberEntity member = memberRepository.findByMemberEmail(memberEmail)
                .orElseThrow(() -> new IllegalArgumentException("Member not found for email: " + memberEmail));

        // 키워드 저장
        KeywordEntity keywordEntity = new KeywordEntity();
        keywordEntity.setKeyword(keyword);
        keywordEntity.setMember(member); // Member와 연관 설정

        // 키워드 저장
        keywordRepository.save(keywordEntity);
    }


}



