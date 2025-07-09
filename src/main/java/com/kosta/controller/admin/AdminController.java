package com.kosta.controller.admin;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kosta.dto.admin.AdminMainPageDto;
import com.kosta.dto.admin.MemberDetailDto;
import com.kosta.dto.admin.MemberListDto;
import com.kosta.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    
    private final AdminService adminService;
    
    @GetMapping("/member/{memberId}")
    public ResponseEntity<MemberDetailDto> getMember(@PathVariable("memberId") String memberId) {
        return ResponseEntity.ok(adminService.getMember(memberId));
    }
    @GetMapping("/main")
    public ResponseEntity<AdminMainPageDto> adminStatus() {
    	AdminMainPageDto adminMainPageDto = adminService.getStatus();
    	return ResponseEntity.ok(adminMainPageDto);
    }
    @GetMapping("/member")
    @PreAuthorize("hasRole('ADMIN')") 
    public ResponseEntity<?> getMemberList(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "sortBy", defaultValue = "memberReg") String sortBy,
            @RequestParam(name = "sortDir", defaultValue = "desc") String sortDir) {
        
        log.info("회원 목록 조회 요청 - page: {}, size: {}, keyword: {}, sortBy: {}, sortDir: {}", 
                page, size, keyword, sortBy, sortDir);
        
        try {
            // 클라이언트는 1부터 시작하는 페이지를 사용하므로 -1
            int adjustedPage = Math.max(0, page - 1);
            
            // 정렬 방향 설정
            Sort.Direction sortDirection = sortDir.equalsIgnoreCase("desc") 
                ? Sort.Direction.DESC : Sort.Direction.ASC;
            
            // 페이징 설정
            Pageable pageable = PageRequest.of(adjustedPage, size, Sort.by(sortDirection, sortBy));
            
            // 회원 목록 조회
            Page<MemberListDto> memberPage;
            if (keyword == null || keyword.trim().isEmpty()) {
                memberPage = adminService.getActiveMemberList(pageable);
            } else {
                memberPage = adminService.searchActiveMemberList(keyword, pageable);
            }
            
            log.info("회원 목록 조회 성공 - 총 {}개 회원, {}페이지", 
                    memberPage.getTotalElements(), memberPage.getTotalPages());
            
            return ResponseEntity.ok(memberPage);
            
        } catch (Exception e) {
            log.error("회원 목록 조회 중 오류 발생", e);
            
            // 에러 정보를 JSON으로 반환
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "회원 목록 조회 실패");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", java.time.LocalDateTime.now().toString());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}