package com.example.Notice_reminder.config;

import com.example.Notice_reminder.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {

    private final UserDetailService userService;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // 정적 리소스 (CSS, JS 등)에 대해 시큐리티 적용 제외
        return (web) -> web.ignoring()
                .requestMatchers("/css/**", "/images/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/home", "/member/login", "/member/signup").permitAll() // 루트와 회원가입, 로그인 허용
                        .requestMatchers("/member/admin").hasAuthority("admin") // 관리자 페이지 접근 제한
                        .anyRequest().authenticated() // 나머지 요청은 인증 필요
                )
                .formLogin(form -> form
                        .loginPage("/member/login") // 로그인 페이지 설정
                        .successHandler(customAuthenticationSuccessHandler) // 로그인 성공 시 핸들러
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/") // 로그아웃 성공 시 루트 페이지로 이동
                        .invalidateHttpSession(true) // 세션 무효화
                )
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화 (개발 시 사용)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userDetailService) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(userService) // 사용자 정보 서비스 설정
                .passwordEncoder(bCryptPasswordEncoder); // 비밀번호 인코더 설정
        return authenticationManagerBuilder.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

