package com.kosta.repository.member;

import com.kosta.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    
    // 로그인 ID로 회원 조회
    Optional<Member> findByMemberId(String memberId);
    
    // 이메일로 회원 조회
    Optional<Member> findByMemberEmail(String memberEmail);
    
    // 활성 상태인 회원만 조회
    Optional<Member> findByMemberIdAndIsActive(String memberId, String isActive);
}