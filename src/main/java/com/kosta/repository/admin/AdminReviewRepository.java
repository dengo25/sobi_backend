package com.kosta.repository.admin;

import com.kosta.domain.member.Member;
import com.kosta.domain.review.Review;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminReviewRepository extends JpaRepository<Review, Long> {
	
	long count();
	
	long countByMember(Member member);
	
	List<Review> findTop5ByMemberOrderByCreatedAtDesc(Member member);

}