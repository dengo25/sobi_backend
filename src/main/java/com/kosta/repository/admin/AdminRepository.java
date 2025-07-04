package com.kosta.repository.admin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kosta.domain.member.Member;



@Repository
public interface AdminRepository extends JpaRepository<Member, Long> {

	@Query("SELECT COUNT(m) FROM Member m WHERE DATE(m.memberReg) = CURRENT_DATE AND m.role = 'ROLE_USER'")
	public long countTodayJoinMembers();
	
	//전체 회원 수
	public long countByRole(String role);
	
	public Member findByMemberId(String memberId);
	public List<Member> findByRole(String role);
	
	
	
	@Query("SELECT m.memberId FROM Member m WHERE m.memberId=:memberId")
	public String findMemberIdByMemberId(String memberId);

}
