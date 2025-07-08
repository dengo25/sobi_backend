package com.kosta.service.report;

import org.springframework.stereotype.Service;

import com.kosta.domain.member.Member;
import com.kosta.domain.report.Report;
import com.kosta.dto.report.ReportDto;
import com.kosta.repository.admin.AdminRepository;
import com.kosta.repository.report.ReportRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService{

	private final ReportRepository reportRepository;
	private final AdminRepository adminRepository;
	
	@Override
	public void saveReport(ReportDto dto) {
		Member reporterId = adminRepository.findByMemberId(dto.getReporterId());
		Member reportedId = adminRepository.findByMemberId(dto.getReportedId());
		
		Report report = new Report();
		report.setReporterId(reporterId);
		report.setReportedId(reportedId);
		report.setDetail(dto.getDetail());
		report.setReportType(dto.getReportType());
		report.setTargetId(dto.getTargetId());
		report.setStatus("PENDING");
		reportRepository.save(report);
	}
}
