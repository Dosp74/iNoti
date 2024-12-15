package com.example.Notice_reminder.service;

import com.example.Notice_reminder.entity.MemberEntity;
import com.example.Notice_reminder.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService { //UserDetailsService: 스프링 시큐리티에서 사용자 정보를 가져오는 인터페이스
    private final MemberRepository memberRepository;

    //email을 통해 MemberEntity 반환
    @Override
    public MemberEntity loadUserByUsername(String email){
        return memberRepository.findByMemberEmail(email)
                .orElseThrow(()->new IllegalArgumentException(email));
    }
}
