package com.kosta.service.admin;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kosta.domain.blacklist.Blacklist;
import com.kosta.domain.member.Member;
import com.kosta.domain.review.Review;
import com.kosta.dto.admin.AdminMainPageDto;
import com.kosta.dto.admin.MemberDetailDto;
import com.kosta.dto.admin.MemberListDto;
import com.kosta.repository.admin.AdminRepository;
import com.kosta.repository.admin.AdminReviewRepository;
import com.kosta.repository.blacklist.BlacklistRepository;
import com.kosta.repository.report.ReportRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AdminService {
	private final AdminRepository adminRepository;
    private final AdminReviewRepository adminReviewRepository;
    private final ReportRepository reportRepository;
    private final BlacklistRepository blacklistRepository;
    
    public MemberDetailDto getMember(String memberId) {
    	Member member = adminRepository.findByMemberId(memberId);
    	List<Review> recentReviews = adminReviewRepository.findTop5ByMemberOrderByCreatedAtDesc(member);
    	long memberReviewCount = adminReviewRepository.countByMemberAndIsDeleted(member,"N");
    	return MemberDetailDto.builder()
    			.memberName(member.getMemberName())
    			.memberId(member.getMemberId())
    			.memberGender(member.getMemberGender())
    			.memberEmail(member.getMemberEmail())
    			.memberAddr(member.getMemberAddr())
    			.memberReg(member.getMemberReg())
    			.memberReviewCount(memberReviewCount)
    			.recentReviews(recentReviews)
    			.build();
    }
    
    public AdminMainPageDto getStatus() {
    	long totlaMemberCount = adminRepository.countByRole("ROLE_USER");
    	long blockedCount = blacklistRepository.countByStatus("BLOCKED");
    	long reviewCount = adminReviewRepository.countByIsDeleted("N");
    	List<Blacklist> blacklist = blacklistRepository.getBlockedMember();
    	long unSolvedReportCount = reportRepository.countByStatus("PENDING");
    	
    	return AdminMainPageDto.builder()
    			.totalMemberCount(totlaMemberCount)
    			.blockedCount(blockedCount)
    			.reviewCount(reviewCount)
    			.blacklist(blacklist)
    			.unSolvedReportCount(unSolvedReportCount)
    			.build();
    }
    
    /**
     * 활성화된 일반 회원 목록 조회 (페이징)
     * @param pageable 페이징 정보
     * @return 회원 목록 페이지
     */
    public Page<MemberListDto> getActiveMemberList(Pageable pageable) {
        return adminRepository.findActiveMembersWithCounts(pageable);
    }
    
    /**
     * 키워드로 활성화된 일반 회원 검색 (페이징)
     * @param keyword 검색 키워드
     * @param pageable 페이징 정보
     * @return 회원 목록 페이지
     */
    public Page<MemberListDto> searchActiveMemberList(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getActiveMemberList(pageable);
        }
        return adminRepository.findActiveMembersWithCountsByKeyword(keyword.trim(), pageable);
    }
}