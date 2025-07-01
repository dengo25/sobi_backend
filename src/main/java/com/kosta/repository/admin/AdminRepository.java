package com.kosta.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kosta.domain.member.Member;

@Repository
public interface AdminRepository extends JpaRepository<Member, Long> {

	@Query("SELECT COUNT(m) FROM Member m WHERE DATE(m.createdAt) = CURRENT_DATE")
	long countTodayJoinMembers();
	public int getTodayJoinMember();
	
	//전체 회원 수
	public long count();
}
