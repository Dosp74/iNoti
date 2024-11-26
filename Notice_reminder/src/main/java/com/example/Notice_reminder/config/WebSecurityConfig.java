package com.example.Notice_reminder.config;

import com.example.Notice_reminder.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration //@Component를 상속받아 자동으로 빈 등록, 시큐리티 설정 클래스
public class WebSecurityConfig {

    private final UserDetailService userService;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {  //css 하위 파일에 대해서 시큐리티 기능 사용x
        return (web) -> web.ignoring()
                .requestMatchers("/css/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {    //http요청에 대한 보안 구성
        return http
                .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers("/member/login","/member/signup").permitAll()
                    .requestMatchers("/member/admin/").hasAuthority("admin")
                    .anyRequest().authenticated()
                )
                .formLogin(form->form
                    .loginPage("/member/login")
                    .successHandler(customAuthenticationSuccessHandler)
                )
                .logout(logout->logout
                    .logoutSuccessUrl("/member/login")
                    .invalidateHttpSession(true)    //로그아웃 이후 세션 삭제
                )
                .csrf(csrf->csrf.disable())
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userDetailService) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(userService)  // 사용자 정보 서비스 설정
                .passwordEncoder(bCryptPasswordEncoder); // 비밀번호 인코더 설정
        return authenticationManagerBuilder.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() { //비밀번호 인코더 빈으로 등록
        return new BCryptPasswordEncoder();
    }
}
