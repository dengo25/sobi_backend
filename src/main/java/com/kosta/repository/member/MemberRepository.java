package com.kosta.repository.member;

import com.kosta.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    
    Member findByMemberName(String memberName);
    Member findByMemberId(String memberId);
    boolean existsByMemberId(String memberId);
    
    Optional<Member> findOptionalByMemberId(String memberId);
    Optional<Member> findByMemberEmail(String memberEmail);
    Optional<Member> findByMemberIdAndIsActive(String memberId, String isActive);
    
    Optional<Member> findByMemberEmailAndIsActive(String memberEmail, String isActive);
    boolean existsByMemberEmail(String memberEmail);
}