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

    private final MemberRepository memberRepository; // 먼저 jpa, mysql dependency 추가
    private final BCryptPasswordEncoder bCryptPasswordEncoder; //

    public Long save(MemberDTO memberDTO) {
        MemberEntity encoding_entity=MemberEntity.toMemberEntity(memberDTO);
        encoding_entity.setMemberPassword(bCryptPasswordEncoder.encode(encoding_entity.getMemberPassword()));
        return memberRepository.save(encoding_entity).getId();
    }

    public List<MemberDTO> findAll() {
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        //Controller로 dto로 변환해서 줘야 함
        List<MemberDTO> memberDTOList = new ArrayList<>();
        for (MemberEntity memberEntity : memberEntityList){
            memberDTOList.add(MemberDTO.toMemberDTO(memberEntity));
        }
        return memberDTOList;
    }

    public MemberDTO findById(Long id) {
        // 하나 조회할때 optional로 감싸줌
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(id);
        if (optionalMemberEntity.isPresent()){
            return MemberDTO.toMemberDTO(optionalMemberEntity.get()); // optional을 벗겨내서 entity -> dto 변환
        }else {
            return null;
        }
    }

    public MemberDTO findByEmail(String email) {
        // 하나 조회할때 optional로 감싸줌
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(email);
        return MemberDTO.toMemberDTO(optionalMemberEntity.get()); // optional을 벗겨내서 entity -> dto 변환
    }

    public void deleteByid(Long id) {
        memberRepository.deleteById(id);
    }
}
//MemberService.class
