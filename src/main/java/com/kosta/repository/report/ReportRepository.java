package com.kosta.repository.report;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kosta.domain.member.Member;
import com.kosta.domain.report.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {

	public List<Report> findByStatus(String status);
	
	public Long countByStatus(String status);
	
	public Long countByReportedId(Member reportedId);
}
