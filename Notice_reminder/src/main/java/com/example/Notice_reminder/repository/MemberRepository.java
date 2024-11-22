package com.example.Notice_reminder.repository;

import com.example.Notice_reminder.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long>//어떤 Entity인지, pk 어떤 타입인지
{
    // 이메일로 회원 정보 조회( select * from member_table where member_email=?)
    // 해당 이메일이 없으면 빈 Optional을, 있으면 해당 MemberEntity 객체를 반환
    Optional<MemberEntity> findByMemberEmail(String memberEmail);
}
//MemberRepository.interface
