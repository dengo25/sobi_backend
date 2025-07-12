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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kosta.dto.admin.AdminMainPageDto;
import com.kosta.dto.admin.MemberDetailDto;
import com.kosta.dto.admin.MemberListDto;
import com.kosta.dto.review.ReviewDTO;
import com.kosta.service.admin.AdminService;
import com.kosta.service.review.ReviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    
    private final AdminService adminService;
    private final ReviewService reviewService;
    
    @GetMapping("/review/{tno}")
    public ReviewDTO get(@PathVariable("tno") Long tno) {
      return reviewService.get(tno);
    }
    
//    @GetMapping("/review/list")
//    public PageResponseDTO<ReviewDTO> list(PageRequestDTO pageRequestDTO) {
//      log.info("list.............. " + pageRequestDTO);
//      
//      return reviewService.getList(pageRequestDTO);
//    }
    
    @GetMapping("/review/list")
    public ResponseEntity<Page<ReviewDTO>> getReviewList(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(name = "sortDir", defaultValue = "desc") String sortDir) {
        
        log.info("리뷰 목록 조회 요청 - page: {}, size: {}, sortBy: {}, sortDir: {}",
                page, size, sortBy, sortDir);
        
        try {
            // 클라이언트는 1부터 시작하는 페이지를 사용하므로 -1
            int adjustedPage = Math.max(0, page - 1);
            
            // 정렬 방향 설정
            Sort.Direction sortDirection = sortDir.equalsIgnoreCase("desc") 
                ? Sort.Direction.DESC : Sort.Direction.ASC;
            
            // 페이징 설정
            Pageable pageable = PageRequest.of(adjustedPage, size, Sort.by(sortDirection, sortBy));
            
            // 리뷰 목록 조회
            Page<ReviewDTO> reviewPage = reviewService.getReviewPage(pageable);
            
            log.info("리뷰 목록 조회 성공 - 총 {}개 리뷰, {}페이지", 
                    reviewPage.getTotalElements(), reviewPage.getTotalPages());
            
            return ResponseEntity.ok(reviewPage);
            
        } catch (Exception e) {
            log.error("리뷰 목록 조회 중 오류 발생", e);
            return ResponseEntity.status(500).build();
        }
    }
    
    @PatchMapping("/review/{tno}/approve")
    public ResponseEntity<String> approveReview(@PathVariable Long tno) {
        return ResponseEntity.ok(adminService.approveReview(tno));
    }

    @PatchMapping("/review/{tno}/reject")
    public ResponseEntity<String> rejectReview(@PathVariable Long tno) {
        return ResponseEntity.ok(adminService.rejectReview(tno));
    }

    @PatchMapping("/review/{tno}/block")
    public ResponseEntity<String> blockReview(@PathVariable Long tno,
                                              @RequestParam(required = false) String reason) {
        return ResponseEntity.ok(adminService.blockReview(tno, reason));
    }
    
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
            @RequestParam(name = "sortBy", defaultValue = "memberReg") String sortBy,
            @RequestParam(name = "sortDir", defaultValue = "desc") String sortDir) {
        
        log.info("회원 목록 조회 요청 - page: {}, size: {}, sortBy: {}, sortDir: {}",
                page, size, sortBy, sortDir);
        
        try {
            // 클라이언트는 1부터 시작하는 페이지를 사용하므로 -1
            int adjustedPage = Math.max(0, page - 1);
            
            // 정렬 방향 설정
            Sort.Direction sortDirection = sortDir.equalsIgnoreCase("desc") 
                ? Sort.Direction.DESC : Sort.Direction.ASC;
            
            // 페이징 설정
            Pageable pageable = PageRequest.of(adjustedPage, size, Sort.by(sortDirection, sortBy));
            
            // 회원 목록 조회
            Page<MemberListDto> memberPage = adminService.getActiveMemberList(pageable);
            
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