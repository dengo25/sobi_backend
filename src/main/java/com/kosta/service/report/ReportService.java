package com.kosta.service.report;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kosta.domain.member.Member;
import com.kosta.domain.report.Report;
import com.kosta.dto.report.ReportDto;
import com.kosta.repository.report.ReportRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportService {

	private final ReportRepository reportRepository;
	
	public List<Report> findByStatus(){
		return reportRepository.findByStatus("PENDING");
	}
	
	public long countByStatus(){
		return reportRepository.countByStatus("PENDING");
	}
	
	public long countByReportedId(Member reportedId) {
		return reportRepository.countByReportedId(reportedId);
	}

	public List<ReportDto> unSolvedReport() {
		List<Report> unSolvedReport = reportRepository.findByStatus("PENDING");
		return unSolvedReport.stream()
				.map(report -> ReportDto.builder().reportType(report.getReportType())
						.reportedId(report.getReportedId().getMemberId())
						.reporterId(report.getReporterId().getMemberId())
						.targetId(report.getTargetId()).detail(report.getDetail()).createdAt(report.getCreatedAt())
						.build())
				.toList();
	}
}
