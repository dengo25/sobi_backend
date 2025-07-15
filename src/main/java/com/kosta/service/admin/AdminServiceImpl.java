package com.kosta.service.admin;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.kosta.dto.blacklist.BlacklistDto;
import com.kosta.dto.review.CategoryDTO;
import com.kosta.dto.review.ReviewImageDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.kosta.domain.blacklist.Blacklist;
import com.kosta.domain.blacklisthistory.BlacklistHistory;
import com.kosta.domain.member.Member;
import com.kosta.domain.report.Report;
import com.kosta.domain.review.Review;
import com.kosta.dto.admin.AdminMainPageDto;
import com.kosta.dto.admin.MemberListDto;
import com.kosta.dto.admin.ReviewListDto;
import com.kosta.dto.admin.ReviewPageResponse;
import com.kosta.dto.admin.ReviewSearchDto;
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
    
    @Override
    public ReviewPageResponse getReviewList(ReviewSearchDto searchDto) {
        log.info("🔍 getReviewList - searchDto: {}", searchDto);
        
        // 빈 문자열을 null로 변환하여 "전체" 조회로 처리
        String confirmedFilter = searchDto.getConfirmed();
        if (confirmedFilter != null && confirmedFilter.trim().isEmpty()) {
            confirmedFilter = null;
        }
        
        log.info("필터 조건 - confirmed: '{}' → '{}'", searchDto.getConfirmed(), confirmedFilter);
        
        // 1. 정렬 조건 설정
        Sort sort = createSort(searchDto.getSortBy(), searchDto.getSortDir());
        
        // 2. 페이징 정보 설정 (클라이언트 1부터 시작 → 서버 0부터 시작)
        int adjustedPage = Math.max(0, searchDto.getPage() - 1);
        Pageable pageable = PageRequest.of(adjustedPage, searchDto.getSize(), sort);
        
        log.info(" 페이징 정보 - page: {} → {}, size: {}, sort: {}", 
                 searchDto.getPage(), adjustedPage, searchDto.getSize(), sort);
        
        // 3. 필터 조건으로 데이터베이스 조회
        Page<Review> reviewPage = adminReviewRepository.findReviewsWithFilters(
            confirmedFilter,
            pageable
        );
        
        log.info(" 조회 결과: {}개 / 총 {}개", reviewPage.getContent().size(), reviewPage.getTotalElements());
        
        // 4. Entity를 DTO로 변환
        List<ReviewListDto> reviewDtos = reviewPage.getContent()
            .stream()
            .map(this::convertToListDto)
            .collect(Collectors.toList());
        
        log.info(" DTO 변환 완료: {}개", reviewDtos.size());
        if (!reviewDtos.isEmpty()) {
            ReviewListDto firstReview = reviewDtos.get(0);
            log.info("첫 번째 리뷰: tno={}, title={}, confirmed={}, memberId={}", 
                     firstReview.getTno(), firstReview.getTitle(), 
                     firstReview.getConfirmed(), firstReview.getMemberId());
        }
        
        // 5. 페이징 응답 객체 생성
        ReviewPageResponse response = ReviewPageResponse.builder()
            .reviews(reviewDtos)
            .totalElements(reviewPage.getTotalElements())
            .totalPages(reviewPage.getTotalPages())
            .currentPage(reviewPage.getNumber()) // 서버는 0부터 시작
            .pageSize(reviewPage.getSize())
            .hasNext(reviewPage.hasNext())
            .hasPrevious(reviewPage.hasPrevious())
            .build();
        
        log.info("📦 응답 객체 생성 완료: reviews={}, totalElements={}, currentPage={}", 
                 response.getReviews().size(), response.getTotalElements(), response.getCurrentPage());
        
        return response;
    }
    
    // 🔧 정렬 조건 생성 메서드
    private Sort createSort(String sortBy, String sortDir) {
        // 허용된 정렬 필드만 사용
        if (!isValidSortField(sortBy)) {
            sortBy = "createdAt"; // 기본값
        }
        
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) 
            ? Sort.Direction.DESC 
            : Sort.Direction.ASC;
        return Sort.by(direction, sortBy);
    }
    
    // 🔧 유효한 정렬 필드 검증
    private boolean isValidSortField(String sortBy) {
        return sortBy != null && (
            "createdAt".equals(sortBy) ||
            "updatedAt".equals(sortBy) ||
            "title".equals(sortBy) ||
            "confirmed".equals(sortBy)
        );
    }
    
    // 🔧 Entity → DTO 변환 메서드
    private ReviewListDto convertToListDto(Review review) {
        return ReviewListDto.builder()
            .tno(review.getRno())
            .title(review.getTitle())
            .content(review.getContent())
            .imageNumber(review.getImageNumber())
            .createdAt(review.getCreatedAt())
            .updatedAt(review.getUpdatedAt())
            .isDeleted(review.getIsDeleted())
            .confirmed(review.getConfirmed())
            .memberId(review.getMember().getMemberId())
            .categoryId(review.getCategory() != null ? review.getCategory().getId() : null)
            .category(review.getCategory() != null ? 
                CategoryDTO.builder()
                    .id(review.getCategory().getId())
                    .name(review.getCategory().getName())
                    .build() : null)
            .images(review.getImages() != null ? 
                review.getImages().stream()
                    .map(img -> ReviewImageDTO.builder()
                        .ino(img.getIno())
                        .reviewId(review.getRno())
                        .fileUrl(img.getFileUrl())
                        .originalFileName(img.getOriginalFileName())
                        .fileType(img.getFileType())
                        .isThumbnail(img.getIsThumbnail())
                        .build())
                    .collect(Collectors.toList()) : null)
            .build();
    }
}
