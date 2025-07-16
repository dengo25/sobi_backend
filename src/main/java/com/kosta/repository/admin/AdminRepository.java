package com.kosta.repository.admin;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kosta.domain.member.Member;
import com.kosta.domain.review.Review;
import com.kosta.dto.admin.MemberListDto;



@Repository
public interface AdminRepository extends JpaRepository<Member, Long> {
    
	Member findByMemberId(String memberId);
	
	@Query("SELECT COUNT(m) FROM Member m WHERE m.role = 'ROLE_USER' " +
			"AND m.isActive = 'Y' " +   
			"AND m.memberId NOT IN (SELECT b.member.memberId FROM Blacklist b WHERE b.status = 'BLOCKED')")
		long countMemberNotBlocked();
    
	@Query("SELECT new com.kosta.dto.admin.MemberListDto(" +
		       "m.memberName, m.memberId, m.memberReg, m.memberGender, m.memberEmail, m.memberAddr, " +
		       "COUNT(DISTINCT r.rno), " +
		       "COUNT(DISTINCT rep.reportId)) " +
		       "FROM Member m " +
		       "LEFT JOIN Review r ON m.memberId = r.member.memberId " +
		       "LEFT JOIN Report rep ON m.memberId = rep.reportedId.memberId " +
		       "WHERE m.isActive = 'Y' " +
		       "AND m.role = 'ROLE_USER' " +
		       "AND m.memberId NOT IN (" +
		       "  SELECT b.member.memberId FROM Blacklist b WHERE b.status = 'BLOCKED'" +
		       ") " +
		       "GROUP BY m.id, m.memberName, m.memberId, m.memberReg, m.memberGender, m.memberEmail, m.memberAddr")
		Page<MemberListDto> findActiveMembersWithCounts(Pageable pageable);
}