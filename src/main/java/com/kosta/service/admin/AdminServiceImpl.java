package com.kosta.service.admin;

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
        long memberReviewCount = adminReviewRepository.countByMemberAndIsDeleted(member, "N");
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
        long totalMemberCount = adminRepository.countByRole("ROLE_USER");
        long blockedCount = blacklistRepository.countByStatus("BLOCKED");
        long reviewCount = adminReviewRepository.countByIsDeleted("N");

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
                .totalMemberCount(totalMemberCount)
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
        review.setConfirmed("B");
        review.setIsDeleted("Y");

        Member member = review.getMember();

        // 블랙리스트 등록
        Blacklist blacklist = Blacklist.builder()
                .member(member)
                .status("BLOCKED")
                .build();
        blacklistRepository.save(blacklist);

        // 블랙리스트 히스토리 등록
        BlacklistHistory history = BlacklistHistory.builder()
                .blacklist(blacklist) // 연관관계
                .reportType("BLOCK")
                .detail(detail)
                .build();
        blacklistHistoryRepository.save(history);

        adminReviewRepository.save(review);
        return "리뷰 차단 및 블랙리스트 등록 완료";
    }
}
