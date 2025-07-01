package com.kosta.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.GetExchange;

import com.kosta.service.admin.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
	
	private final AdminService adminService;
	
	@GetMapping("/member/count")
	public ResponseEntity<Long> getMemberCOunt(){
		long count = adminService.getMemberCount();
		return ResponseEntity.ok(count);
	}
	
	@GetMapping("/member/todayJoin")
	public ResponseEntity<Long> getTodayJoinCount(){
		long count = adminService.getTodayJoinMember();
		return ResponseEntity.ok(count);
	}
}
