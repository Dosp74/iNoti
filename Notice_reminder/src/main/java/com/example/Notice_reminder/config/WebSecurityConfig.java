package com.example.Notice_reminder.config;

import com.example.Notice_reminder.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//스프링 시큐리티 설정 클래스
@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {
    private final UserDetailService userService;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        //css 하위 파일에 대해서 시큐리티를 적용하지 않음
        return (web) -> web.ignoring()
                .requestMatchers("/css/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                //아무나 /login, /signup 접속 가능, 관리자만 /admin 접속 가능, 그 외 모든 주소는 인증 계정이라면 접속 가능
                .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers("/member/login","/member/signup").permitAll()
                    .requestMatchers("/member/admin/").hasAuthority("admin")
                    .anyRequest().authenticated()
                )
                //로그인 페이지를 /member/login으로 지정, 로그인 성공 시 설정한 핸들러 실행
                .formLogin(form->form
                    .loginPage("/member/login")
                    .successHandler(customAuthenticationSuccessHandler)
                )
                //로그아웃 성공 시 /member/login으로 이동, //로그아웃 이후 세션 삭제
                .logout(logout->logout
                    .logoutSuccessUrl("/member/login")
                    .invalidateHttpSession(true)
                )
                .csrf(csrf->csrf.disable())
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userDetailService) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(userService)  // 사용자 정보를 가져올 서비스 설정
                .passwordEncoder(bCryptPasswordEncoder); // 비밀번호 인코더 설정
        return authenticationManagerBuilder.build();
    }

    @Bean
    //비밀번호 인코더 빈으로 등록
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
