package com.kosta.dto.admin;

import java.time.LocalDateTime;
import java.util.List;

import com.kosta.domain.review.Review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDetailDto {
	private String memberName;
	private String memberId;
	private String memberGender;
	private String memberEmail;
	private String memberAddr;
	private LocalDateTime memberReg;
	private long memberReviewCount;
	private List<Review> recentReviews;
}
