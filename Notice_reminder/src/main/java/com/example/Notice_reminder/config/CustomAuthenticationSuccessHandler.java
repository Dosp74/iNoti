package com.example.Notice_reminder.config;
import com.example.Notice_reminder.entity.MemberEntity;
import com.example.Notice_reminder.repository.MemberRepository;
import jakarta.servlet.ServletException;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;

//인증을 성공했을 때, 실행시킬 핸들러
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private MemberRepository memberRepository; // 필드 주입

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String email=authentication.getName();
        MemberEntity memberEntity=memberRepository.findByMemberEmail(email).orElseThrow(()->new IllegalArgumentException(email));
        memberEntity.setLoginAt(LocalDateTime.now());
        memberRepository.save(memberEntity);
        for (GrantedAuthority authority : authorities) {
            //가진 권한이 'admin'이라면 /member/admin으로 이동
            if (authority.getAuthority().equals("admin")) {
                response.sendRedirect("/member/admin");
                return;
            }
        }
        //'user'라면 /member/main으로 이동
        response.sendRedirect("/member/main");
    }
}
