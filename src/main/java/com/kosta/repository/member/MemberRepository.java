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

	Member findByMemberIdAndIsActive(String memberId, String isActive);

	boolean existsByMemberIdAndIsActive(String memberId, String isActive);

	Optional<Member> findByIdAndIsActive(Long id, String isActive);
}