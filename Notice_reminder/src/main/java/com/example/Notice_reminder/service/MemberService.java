package com.example.Notice_reminder.service;

import com.example.Notice_reminder.dto.MemberDTO;
import com.example.Notice_reminder.entity.MemberEntity;
import com.example.Notice_reminder.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(MemberDTO memberDTO) {
        MemberEntity encoding_entity = MemberEntity.toMemberEntity(memberDTO);
        encoding_entity.setMemberPassword(bCryptPasswordEncoder.encode(encoding_entity.getMemberPassword()));
        return memberRepository.save(encoding_entity).getId();
    }

    public List<MemberDTO> findAll() {
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        List<MemberDTO> memberDTOList = new ArrayList<>();
        for (MemberEntity memberEntity : memberEntityList) {
            memberDTOList.add(MemberDTO.toMemberDTO(memberEntity));
        }
        return memberDTOList;
    }

    public MemberDTO findById(Long id) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(id);
        return optionalMemberEntity.map(MemberDTO::toMemberDTO).orElse(null);
    }

    public MemberDTO findByEmail(String email) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(email);
        MemberEntity memberEntity = optionalMemberEntity.orElseThrow(() ->
                new IllegalArgumentException("Member with email " + email + " not found."));
        return MemberDTO.toMemberDTO(memberEntity);
    }

    public void deleteByid(Long id) {
        memberRepository.deleteById(id);
    }

    public List<String> findAllEmails() {
        return memberRepository.findAll().stream()
                .map(MemberEntity::getMemberEmail)
                .toList();
    }

    // 비밀번호 변경 로직
    public boolean changePassword(String memberEmail, String currentPassword, String newPassword) {
        // 사용자 정보 가져오기
        MemberEntity member = memberRepository.findByMemberEmail(memberEmail)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 현재 비밀번호 검증
        if (!bCryptPasswordEncoder.matches(currentPassword, member.getMemberPassword())) {
            return false; // 현재 비밀번호가 일치하지 않으면 실패
        }

        // 기존 비밀번호와 새 비밀번호가 같은지 확인
        if (bCryptPasswordEncoder.matches(newPassword, member.getMemberPassword())) {
            return false; // 새 비밀번호가 기존 비밀번호와 동일하면 실패
        }

        // 새 비밀번호로 업데이트
        member.setMemberPassword(bCryptPasswordEncoder.encode(newPassword));
        memberRepository.save(member);
        return true;
    }
}

