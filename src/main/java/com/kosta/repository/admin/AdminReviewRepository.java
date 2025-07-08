package com.kosta.repository.admin;

import com.kosta.domain.reivew.Review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminReviewRepository extends JpaRepository<Review, Long> {
	
	long countByIsDeleted(String isDeleted);
}