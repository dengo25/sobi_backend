package com.kosta.service.admin;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.kosta.dto.blacklist.BlacklistDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kosta.domain.blacklist.Blacklist;
import com.kosta.domain.blacklisthistory.BlacklistHistory;
import com.kosta.domain.member.Member;
import com.kosta.domain.report.Report;
import com.kosta.domain.review.Review;
import com.kosta.dto.admin.AdminMainPageDto;
import com.kosta.dto.admin.MemberDetailDto;
import com.kosta.dto.admin.MemberListDto;
import com.kosta.repository.admin.AdminRepository;
import com.kosta.repository.admin.AdminReviewRepository;
import com.kosta.repository.blacklist.BlacklistRepository;
import com.kosta.repository.blacklisthistory.BlacklistHistoryRepository;
import com.kosta.repository.report.ReportRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final AdminReviewRepository adminReviewRepository;
    private final ReportRepository reportRepository;
    private final BlacklistRepository blacklistRepository;
    private final BlacklistHistoryRepository blacklistHistoryRepository;
    
    @Override
    public MemberDetailDto getMember(String memberId) {
        Member member = adminRepository.findByMemberId(memberId);
        long memberReviewCount = adminReviewRepository.countByMember(member);
        return MemberDetailDto.builder()
                .memberName(member.getMemberName())
                .memberId(member.getMemberId())
                .memberGender(member.getMemberGender())
                .memberEmail(member.getMemberEmail())
                .memberAddr(member.getMemberAddr())
                .memberReg(member.getMemberReg())
                .memberReviewCount(memberReviewCount)
                .build();
    }

    @Override
    public AdminMainPageDto getStatus() {
        long memberNotBlockedCount = adminRepository.countMemberNotBlocked();
        long blockedCount = blacklistRepository.countByStatus("BLOCKED");
        long reviewCount = adminReviewRepository.count();

        List<Blacklist> blacklistEntities = blacklistRepository.getBlockedMember();
        List<BlacklistDto> blacklistDtos = blacklistEntities.stream()
                .map(bl -> BlacklistDto.builder()
                        .blackListNo(bl.getBlackListNo())
                        .memberId(bl.getMember().getMemberId())
                        .memberName(bl.getMember().getMemberName())
                        .updateAt(bl.getUpdateAt())
                        .build())
                .collect(Collectors.toList());

        long unSolvedReportCount = reportRepository.countByStatus("PENDING");

        return AdminMainPageDto.builder()
                .memberNotBlockedCount(memberNotBlockedCount)
                .blockedCount(blockedCount)
                .reviewCount(reviewCount)
                .blacklistDto(blacklistDtos)
                .unSolvedReportCount(unSolvedReportCount)
                .build();
    }

    @Override
    public Page<MemberListDto> getActiveMemberList(Pageable pageable) {
        return adminRepository.findActiveMembersWithCounts(pageable);
    }
    
    @Override
    public String approveReview(Long tno) {
        Optional<Review> result = adminReviewRepository.findById(tno);
        Review review = result.orElseThrow(() -> new RuntimeException("리뷰가 존재하지 않습니다."));
        
        review.setConfirmed("Y");
        adminReviewRepository.save(review);
        return "리뷰 승인 완료";
    }

    @Override
    public String rejectReview(Long tno) {
        Optional<Review> result = adminReviewRepository.findById(tno);
        Review review = result.orElseThrow(() -> new RuntimeException("리뷰가 존재하지 않습니다."));

        review.setConfirmed("R");
        adminReviewRepository.save(review);
        return "리뷰 반려 완료";
    }

	@Override
	public String blockReview(Long tno, String detail) {
		Optional<Review> result = adminReviewRepository.findById(tno);
		Review review = result.orElseThrow(() -> new RuntimeException("리뷰가 존재하지 않습니다."));

		Member member = review.getMember();

		Optional<Blacklist> blacklistResult = blacklistRepository.findByMember(member);
		Blacklist blacklist = null;

		if (blacklistResult.isPresent()) {
			blacklist = blacklistResult.get();
			if (!"BLOCKED".equals(blacklist.getStatus())) {
				blacklist.setStatus("BLOCKED");
				blacklist.setUpdateAt(LocalDateTime.now());
			}
		} else {
			blacklist = Blacklist.builder().member(member).status("BLOCKED").build();
		}
		blacklistRepository.save(blacklist);

		// 블랙리스트 히스토리 등록
		BlacklistHistory history = BlacklistHistory.builder().blacklist(blacklist) // 연관관계
				.reportType("BLOCK").detail(detail).build();
		blacklistHistoryRepository.save(history);

		member.setIsActive("N");
		adminRepository.save(member);
		List<Report> pendingReports = reportRepository.findPendingReportsByTargetId(tno.intValue());

		if (!pendingReports.isEmpty()) {
			for (Report report : pendingReports) {
				report.setStatus("APPROVE");
			}
			reportRepository.saveAll(pendingReports);
			log.info("리뷰 차단으로 인해 {}건의 신고가 처리완료로 변경되었습니다.", pendingReports.size());
		}
		
		adminReviewRepository.delete(review);

		return "리뷰 차단 및 블랙리스트 등록 완료";
	}
    
	@Override
	public String approveReport(int reportId, Long tno, String detail) {
		Optional<Report> reportResult = reportRepository.findById(reportId);
		Report report = reportResult.orElseThrow(() -> new RuntimeException("신고내역이 존재하지 않습니다."));
		report.setStatus("APPROVE");

		Optional<Review> reviewResult = adminReviewRepository.findById(tno);
		Review review = reviewResult.orElseThrow(() -> new RuntimeException("리뷰가 존재하지 않습니다."));

		Member member = review.getMember();
		Optional<Blacklist> blacklistResult = blacklistRepository.findByMember(member);
		Blacklist blacklist = null;

		if (blacklistResult.isPresent()) {
			blacklist = blacklistResult.get();
			if (!"BLOCKED".equals(blacklist.getStatus())) {
				blacklist.setStatus("BLOCKED");
				blacklist.setUpdateAt(LocalDateTime.now());
			} 		
		}else {
			blacklist = Blacklist.builder().member(member).status("BLOCKED").build();
		}
		blacklistRepository.save(blacklist);

		// 블랙리스트 히스토리 등록
		BlacklistHistory history = BlacklistHistory.builder().blacklist(blacklist).reportType("BLOCK").detail(detail)
				.build();
		blacklistHistoryRepository.save(history);

		member.setIsActive("N");
		adminRepository.save(member);

		List<Report> pendingReports = reportRepository.findPendingReportsByTargetId(tno.intValue());

		if (!pendingReports.isEmpty()) {
			for (Report r : pendingReports) {
				r.setStatus("APPROVE");
			}
			reportRepository.saveAll(pendingReports);
			log.info("리뷰 차단으로 인해 {}건의 신고가 처리완료로 변경되었습니다.", pendingReports.size());
		}

		adminReviewRepository.delete(review);
		reportRepository.save(report);

		return "신고 승인 및 리뷰 삭제 처리";
	}
    
    
    @Override 
    public String rejectReport(int reportId) {
    	Report report = reportRepository.findById(reportId)
    			.orElseThrow(() -> new RuntimeException("신고내역이 존재하지 않습니다."));
    	
    	if(!"PENDING".equals(report.getStatus())) {
    		throw new RuntimeException("이미 처리된 신고입니다. 현재 상태: " + report.getStatus());
    	}
    	report.setStatus("REJECT");
    	
    	reportRepository.save(report);
    	
    	log.info("신고 반려 완료 - reportId: {}", reportId);
    	return "신고 반려 처리";
    }
}
