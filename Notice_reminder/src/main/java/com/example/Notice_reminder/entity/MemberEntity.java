package com.example.Notice_reminder.entity;

import com.example.Notice_reminder.dto.MemberDTO;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "member_table") //database에 해당 이름의 테이블 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED) //기본 생성자
public class MemberEntity implements UserDetails { //table 역할
    //jpa ==> database를 객체처럼 사용 가능

    @Id //id필드 기본키로 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    @Column(updatable = false) //id 변경 불가
    private Long id;

    @Column(nullable = false, unique = true)
    private String memberEmail;

    @Column
    private String memberPassword;

    @Column
    private String memberName;

    public static MemberEntity toMemberEntity(MemberDTO memberDTO){
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setId(memberDTO.getId());
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setMemberName(memberDTO.getMemberName());
        memberEntity.setMemberPassword(memberDTO.getMemberPassword());
        return memberEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of(new SimpleGrantedAuthority("user")); //사용자 권한 반환, 이후 관리자 권한 구성 필요
    }

    @Override
    public String getUsername(){
        return memberEmail;
    }

    @Override
    public String getPassword(){
        return memberPassword;
    }

    @Override
    public boolean isAccountNonExpired(){ //계정 만료 여부
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){ //계정 잠금 여부
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired(){ //패스워드 만료 여부
        return true;
    }

    @Override
    public boolean isEnabled(){ //계정 사용 가능 여부
        return true;
    }
}
//MemberEntity.class
