package com.kosta.repository.member;

import com.kosta.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member , Long> {
  
  Member findByMemberName(String memberEmail);
  Member findByMemberId(String memberId);
  boolean existsByMemberId(String memberId);
}
