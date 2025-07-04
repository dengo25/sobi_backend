package com.kosta.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kosta.domain.member.Member;



@Repository
public interface AdminRepository extends JpaRepository<Member, Long> {

	@Query("SELECT COUNT(m) FROM Member m WHERE DATE(m.memberReg) = CURRENT_DATE")
	public long countTodayJoinMembers();
	
	//전체 회원 수
	public long count();
	
	public Member findByMemberId(String memberId);
	
	@Query("SELECT m.memberId FROM Member m WHERE m.id=:id")
	public String findMemberIdById(Long id);
}
