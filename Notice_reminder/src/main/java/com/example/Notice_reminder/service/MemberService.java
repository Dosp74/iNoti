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

@Service //스프링이 관리해주는 객체 == 스프링 빈
@RequiredArgsConstructor //controller와 같이. final 멤버변수 생성자 만드는 역할( memberRepository, bCryptPasswordEncoder )
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(MemberDTO memberDTO) {
        MemberEntity encoding_entity=MemberEntity.toMemberEntity(memberDTO);
        encoding_entity.setMemberPassword(bCryptPasswordEncoder.encode(encoding_entity.getMemberPassword()));
        return memberRepository.save(encoding_entity).getId();
    }

    public List<MemberDTO> findAll() {
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        //Controller에게 dto로 변환해서 줘야 함
        List<MemberDTO> memberDTOList = new ArrayList<>();
        for (MemberEntity memberEntity : memberEntityList){
            memberDTOList.add(MemberDTO.toMemberDTO(memberEntity));
        }
        return memberDTOList;
    }

    public MemberDTO findByEmail(String email) {
        // 이메일로 MemberEntity 조회
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(email);

        // MemberEntity가 존재하지 않으면 예외 처리
        MemberEntity memberEntity = optionalMemberEntity.orElseThrow(() ->
                new IllegalArgumentException("Member with email " + email + " not found.")
        );

        // Entity를 DTO로 변환하여 반환
        return MemberDTO.toMemberDTO(memberEntity);
    }

    public void deleteByid(Long id) {
        memberRepository.deleteById(id);
    }

    public List<String> findAllEmails() {
        return memberRepository.findAll().stream()
                .map(MemberEntity::getMemberEmail) // MemberEntity에서 memberEmail 추출
                .toList();
    }
}
//MemberService.class
