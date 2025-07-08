package com.kosta.repository.report;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kosta.domain.member.Member;
import com.kosta.domain.report.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
    // 특정 회원이 신고 받은 횟수 조회
    long countByReportedIdMemberId(String memberId);
    long countByStatus(String status);
}