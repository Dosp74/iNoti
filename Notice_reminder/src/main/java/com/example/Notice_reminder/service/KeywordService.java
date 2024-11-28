package com.example.Notice_reminder.service;

import com.example.Notice_reminder.entity.KeywordEntity;
import com.example.Notice_reminder.repository.KeywordRepository;
import com.example.Notice_reminder.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeywordService {

    private final KeywordRepository keywordRepository;
    private final MemberRepository memberRepository;

    public KeywordService(KeywordRepository keywordRepository, MemberRepository memberRepository) {
        this.keywordRepository = keywordRepository;
        this.memberRepository = memberRepository;
    }

    public void saveKeyword(String keyword, String memberEmail) {
        // 사용자의 정보를 이메일로 조회
        var member = memberRepository.findByMemberEmail(memberEmail)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + memberEmail));

        // 키워드 저장
        KeywordEntity keywordEntity = new KeywordEntity();
        keywordEntity.setKeyword(keyword);
        keywordEntity.setMember(member);

        keywordRepository.save(keywordEntity);
    }

    public List<KeywordEntity> findKeywordsByMemberEmail(String memberEmail) {
        // 사용자의 정보를 이메일로 조회
        var member = memberRepository.findByMemberEmail(memberEmail)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + memberEmail));

        // 해당 사용자가 등록한 키워드 조회
        return keywordRepository.findByMember(member);
    }
    public void deleteKeyword(Long keywordId) {
        keywordRepository.deleteById(keywordId);
    }

}




