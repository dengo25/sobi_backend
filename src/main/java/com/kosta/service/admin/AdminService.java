package com.kosta.service.admin;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kosta.domain.member.Member;
import com.kosta.domain.memberlog.Memberlog;
import com.kosta.dto.admin.AdminMainPageDto;
import com.kosta.dto.admin.MemberDetailDto;
import com.kosta.dto.admin.MemberListDto;
import com.kosta.dto.admin.PageRequestDTO;
import com.kosta.dto.admin.PageResponse;
import com.kosta.dto.admin.PageResponseDTO;
import com.kosta.dto.review.ReviewDTO;
import com.kosta.repository.admin.AdminRepository;
import com.kosta.repository.blacklist.BlacklistRepository;
import com.kosta.repository.member.MemberRepository;
import com.kosta.repository.memberlog.MemberLogRepository;
import com.kosta.repository.report.ReportRepository;
import com.kosta.repository.review.ReviewRepository;

import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
@Data
public class AdminService {

    private final MemberRepository memberRepository;
	private final AdminRepository adminRepository;
	private final BlacklistRepository blacklistRepository;
	private final ReviewRepository reviewRepository;
	private final MemberLogRepository logRepository;
	private final ReportRepository reportRepository;
	
	public AdminMainPageDto getAdminStats() {
		long totalMemberCount = adminRepository.countByRole("ROLE_USER");
		long todayJoinCount = adminRepository.countTodayJoinMembers();
		long blockedCount = blacklistRepository.countByStatus("BLOCKED");
		long reviewCount = reviewRepository.count();
		List<Memberlog> memberlogs = logRepository.findAll();
		long unSolvedReportCount = reportRepository.countByStatus("PENDING");
		return AdminMainPageDto.builder()
				.totalMemberCount(totalMemberCount)
				.todayJoinCount(todayJoinCount)
				.blockedCount(blockedCount)
				.reviewCount(reviewCount)
				.memberlogs(memberlogs)
				.unSolvedReportCount(unSolvedReportCount)
				.build();
	}
	public List<Member> findAll(){
		return adminRepository.findAll();
	}
	
    public Page<MemberListDto> getMemberList(PageRequestDTO requestDTO) {
        return adminRepository.searchMemberList(requestDTO);
    }
	
	public MemberDetailDto memberDetailDto(String memberId) {
		Member member = adminRepository.findByMemberId(memberId);
		long reviewCount = reviewRepository.countByMember(member);
		
		return MemberDetailDto.builder()
				.memberName(member.getMemberName())
				.memberAddr(member.getMemberAddr())
				.memberGender(member.getMemberGender())
				.memberEmail(member.getMemberEmail())
				.memberId(memberId)
				.memberReg(member.getMemberReg())
				.memberReviewCount(reviewCount)
				.isActive(member.getIsActive())
				.build();
	}
}
