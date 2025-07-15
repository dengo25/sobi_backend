package com.kosta.repository.member;

import com.kosta.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    
    Member findByMemberName(String memberEmail);
    
    Member findByMemberId(String memberId);
    
    boolean existsByMemberId(String memberId);
    
    // 활성 사용자만 조회하는 메소드들 추가
    Member findByMemberIdAndIsActive(String memberId, String isActive);
    
    boolean existsByMemberIdAndIsActive(String memberId, String isActive);
    
    Optional<Member> findByIdAndIsActive(Long id, String isActive);
    
    // 이메일 중복 확인을 위한 메서드 추가
    boolean existsByMemberEmailAndIsActive(String memberEmail, String isActive);
    
    // 이메일로 회원 조회 (활성 사용자만)
    Optional<Member> findByMemberEmailAndIsActive(String memberEmail, String isActive);
}