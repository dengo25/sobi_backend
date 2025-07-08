package com.kosta.repository.admin;

import com.kosta.domain.member.Member;
import com.kosta.domain.reivew.Review;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminReviewRepository extends JpaRepository<Review, Long> {
	
	long countByIsDeleted(String isDeleted);
	
	long countByMemberAndIsDeleted(Member member, String isDeleted);
	
	List<Review> findTop5ByMemberOrderByCreatedAtDesc(Member member);
}