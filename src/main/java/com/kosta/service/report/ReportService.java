//package com.kosta.service.report;
//
//import java.util.List;
//
//import org.springframework.stereotype.Service;
//
//import com.kosta.domain.member.Member;
//import com.kosta.domain.report.Report;
//import com.kosta.dto.report.ReportDto;
//import com.kosta.repository.admin.AdminRepository;
//import com.kosta.repository.report.ReportRepository;
//
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//
//@Service
//@RequiredArgsConstructor
//@Transactional
//public class ReportService {
//
//    private final AdminRepository adminRepository;
//	private final ReportRepository reportRepository;
//	
//	public List<Report> findByStatus(){
//		return reportRepository.findByStatus("PENDING");
//	}
//	
//	public long countByStatus(){
//		return reportRepository.countByStatus("PENDING");
//	}
//	
//	public long countByReportedId(Member reportedId) {
//		return reportRepository.countByReportedId(reportedId);
//	}
//
//	public List<ReportDto> unSolvedReport() {
//		List<Report> unSolvedReport = reportRepository.findByStatus("PENDING");
//		return unSolvedReport.stream()
//				.map(report -> ReportDto.builder().reportCategory(report.getReportCategory())
//						.reportedId(adminRepository.findMemberIdById(report.getReportedId().getId()))
//						.reporterId(adminRepository.findMemberIdById(report.getReporterId().getId()))
//						.targetId(report.getTargetId()).detail(report.getDetail()).createdAt(report.getCreatedAt())
//						.build())
//				.toList();
//	}
//}
