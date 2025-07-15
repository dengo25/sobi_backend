package com.kosta.repository.admin;

import com.kosta.domain.member.Member;
import com.kosta.domain.review.Review;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminReviewRepository extends JpaRepository<Review, Long> {
	
	long count();
	
	long countByMember(Member member);
	
	List<Review> findTop5ByMemberOrderByCreatedAtDesc(Member member);
	
    @Query("SELECT r FROM Review r " +
            "WHERE (:confirmed IS NULL OR :confirmed = '' OR r.confirmed = :confirmed)")
     Page<Review> findReviewsWithFilters(
         @Param("confirmed") String confirmed,
         Pageable pageable
     );
}