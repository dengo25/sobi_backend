package com.kosta.service.admin;

import com.kosta.dto.admin.AdminMainPageDto;
import com.kosta.dto.admin.MemberListDto;
import com.kosta.dto.admin.ReviewPageResponse;
import com.kosta.dto.admin.ReviewSearchDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {
    
    // 관리자 메인 통계 조회
    AdminMainPageDto getStatus();
    
    // 회원 리스트 조회 (페이징)
    Page<MemberListDto> getActiveMemberList(Pageable pageable);
    
	String approveReview(Long tno);

	String rejectReview(Long tno);

	String blockReview(Long tno, String detail);

	String approveReport(int reportId, Long tno, String detail);
	
	String rejectReport(int reportId);
	
	ReviewPageResponse getReviewList(ReviewSearchDto searchDto);

}
