package com.kosta.service.admin;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kosta.domain.member.Member;
import com.kosta.domain.memberlog.Memberlog;
import com.kosta.dto.admin.AdminMainPageDto;
import com.kosta.dto.admin.MemberListDto;
import com.kosta.dto.admin.PagedMemberListDto;
import com.kosta.repository.admin.AdminRepository;
import com.kosta.repository.admin.AdminReviewRepository;
import com.kosta.repository.blacklist.BlacklistRepository;
import com.kosta.repository.member.MemberRepository;
import com.kosta.repository.memberlog.MemberLogRepository;
import com.kosta.repository.report.ReportRepository;
import com.kosta.repository.review.ReviewRepository;

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
    private final MemberLogRepository memberlogRespository;
    
    public AdminMainPageDto getStatus() {
    	long totlaMemberCount = adminRepository.countByRole("ROLE_USER");
    	long blockedCount = blacklistRepository.countByStatus("BLOCKED");
    	long reviewCount = adminReviewRepository.countByIsDeleted("N");
    	List<Memberlog> memberlogList = memberlogRespository.findAll();
    	long unSolvedReportCount = reportRepository.countByStatus("PENDING");
    	
    	return AdminMainPageDto.builder()
    			.totalMemberCount(totlaMemberCount)
    			.blockedCount(blockedCount)
    			.reviewCount(reviewCount)
    			.memberlogs(memberlogList)
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