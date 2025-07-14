package com.kosta.repository.review;

import com.kosta.domain.review.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
  
  @Query("select ri from ReviewImage ri where ri.review.rno = :rno and ri.isThumbnail = 'Y'")
  Optional<ReviewImage> findThumbnailByReviewId(@Param("rno") Long rno);
}
