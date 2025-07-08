package com.kosta.repository.admin;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kosta.domain.member.Member;
import com.kosta.dto.admin.MemberListDto;

import java.time.LocalDateTime;


@Repository
public interface AdminRepository extends JpaRepository<Member, Long> {
    
    //role 사용자 수
    long countByRole(String role);
    
    @Query("SELECT new com.kosta.dto.admin.MemberListDto(" +
           "m.memberName, m.memberId, m.memberReg, " +
           "COUNT(DISTINCT r.rno), " +
           "COUNT(DISTINCT rep.reportId)) " +
           "FROM Member m " +
           "LEFT JOIN Review r ON m.memberId = r.member.memberId AND r.isDeleted = 'N' " +
           "LEFT JOIN Report rep ON m.memberId = rep.reportedId.memberId " +
           "WHERE m.isActive = 'Y' AND m.role = 'ROLE_USER' " +
           "GROUP BY m.id, m.memberName, m.memberId, m.memberReg " +
           "ORDER BY m.memberReg DESC")
    Page<MemberListDto> findActiveMembersWithCounts(Pageable pageable);
    
    @Query("SELECT new com.kosta.dto.admin.MemberListDto(" +
           "m.memberName, m.memberId, m.memberReg, " +
           "COUNT(DISTINCT r.rno), " +
           "COUNT(DISTINCT rep.reportId)) " +
           "FROM Member m " +
           "LEFT JOIN Review r ON m.memberId = r.member.memberId AND r.isDeleted = 'N' " +
           "LEFT JOIN Report rep ON m.memberId = rep.reportedId.memberId " +
           "WHERE m.isActive = 'Y' AND m.role = 'ROLE_USER' " +
           "AND (m.memberName LIKE %:keyword% OR m.memberId LIKE %:keyword%) " +
           "GROUP BY m.id, m.memberName, m.memberId, m.memberReg " +
           "ORDER BY m.memberReg DESC")
    Page<MemberListDto> findActiveMembersWithCountsByKeyword(@Param("keyword") String keyword, Pageable pageable);

}