package com.kosta.service.search;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.kosta.domain.member.Member;
import com.kosta.dto.admin.MemberListDto;
import com.kosta.dto.admin.PageRequestDTO;
import com.querydsl.jpa.JPQLQuery;

import lombok.extern.log4j.Log4j2;
@Log4j2
public class MemberSearchImpl extends QuerydslRepositorySupport implements MemberSearch {
	
	public MemberSearchImpl() {
		super(Member.class);
	}

	@Override
	public Page<MemberListDto> searchMemberList(PageRequestDTO pageRequestDTO) {
		QMember member = QMember.member;
		QReview  review = QReview.review;
		QReport report = QReport.report;
		
		JPQLQuery<Member> query = from(member)
				.where(member.role.eq("ROLE_USER")); //일반 사용자 대상
		
		//페이징 설정
		Pageable pageable = PageRequest.of(
				pageRequestDTO.getPage() - 1,
				pageRequestDTO.getSize(),
				Sort.by("id").descending()
		);
		getQuerydsl().applyPagination(pageable, query);
		
		List<MemberListDto> dtoList = query.fetch().stream()
				.map(m -> {
					long reviewCount = from(review)
							.where(review.member.eq(m))
							.fetchCount();
					long reportCount = from(report)
							.where(report.reportedId.eq(m))
							.fetchCount();
					return MemberListDto.builder()
							.memberName(m.getMemberName())
							.memberId(m.getMemberId())
							.memberReg(m.getMemberReg())
							.memberReviewCount(reviewCount)
							.memberReportCount(reportCount)
							.build();
				}).collect(Collectors.toList());
		long total = query.fetchCount();
		
		return new PageImpl<>(dtoList, pageable, total);
	}
}
