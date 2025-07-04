package com.kosta.controller.admin;

import java.util.List;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.kosta.dto.admin.AdminMainPageDto;
import com.kosta.dto.admin.MemberDetailDto;
import com.kosta.dto.admin.MemberListDto;
import com.kosta.dto.report.ReportDto;
import com.kosta.service.admin.AdminService;
import com.kosta.service.report.ReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
	
	private final AdminService adminService;
	private final ReportService reportService;
	
	@GetMapping("/main")
	public ResponseEntity<AdminMainPageDto> getMemberCount(){
		AdminMainPageDto stats = adminService.getAdminStats();
		return ResponseEntity.ok(stats);
	}
	@GetMapping("/member")
	public ResponseEntity<List<MemberListDto>> getMemberList(){
		List<MemberListDto> memberList = adminService.memberListDto();
		return ResponseEntity.ok(memberList);
	}

	@GetMapping("/member/{memberId}")
	public ResponseEntity<MemberDetailDto> getMemberDetail(@PathVariable("memberId") String memberId){
		MemberDetailDto memberDetailDto = adminService.memberDetailDto(memberId);
		return ResponseEntity.ok(memberDetailDto);
	}
	@GetMapping("/report")
	public ResponseEntity<List<ReportDto>> unResolvedReport(){
		List<ReportDto> unResolvedReport = reportService.unSolvedReport();
		return ResponseEntity.ok(unResolvedReport);
	}
}
