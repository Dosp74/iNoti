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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "member_table") //database에 해당 이름의 테이블 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEntity implements UserDetails { //UserDetails 인터페이스를 구현하여 인증 객체로 사용

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false) //id 변경 불가
    private Long id;

    @Column(nullable = false, unique = true, updatable = false) //이메일 null값 불가, 중복 불가, 변경 불가
    private String memberEmail;

    @Column(nullable = false)
    private String memberPassword;

    @Column(nullable = false, updatable = false)
    private String memberName;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KeywordEntity> keywords = new ArrayList<>();

    public static MemberEntity toMemberEntity(MemberDTO memberDTO){
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setId(memberDTO.getId());
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setMemberName(memberDTO.getMemberName());
        memberEntity.setMemberPassword(memberDTO.getMemberPassword());
        return memberEntity;
    }

    @Override
    //사용자가 가지고 있는 권한의 목록을 반환
    public Collection<? extends GrantedAuthority> getAuthorities(){
        if("root".equals(this.getMemberEmail()))
            return List.of(new SimpleGrantedAuthority("admin")); //이메일이 "root"라면 관리자 권한 인가
        return List.of(new SimpleGrantedAuthority("user")); //이외에는 사용자 권한 인가
    }

    @Override
    //사용자 id 반환
    public String getUsername(){
        return memberEmail;
    }

    @Override
    //사용자 password 반환
    public String getPassword(){
        return memberPassword;
    }

    @Override
    //계정 만료 여부(만료되지 않았으면 true)
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    //계정 잠금 여부(잠기지 않았으면 true)
    public boolean isAccountNonLocked(){
        return true;
    }

    @Override
    //패스워드 만료 여부(만료되지 않았으면 true)
    public boolean isCredentialsNonExpired(){
        return true;
    }

    @Override
    //계정 사용 가능 여부(사용 가능하면 true)
    public boolean isEnabled(){
        return true;
    }
}

//MemberEntity.class
