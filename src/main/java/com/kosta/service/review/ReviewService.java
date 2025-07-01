package com.kosta.service.review;

import com.kosta.domain.member.Member;
import com.kosta.domain.reivew.Category;
import com.kosta.domain.reivew.Review;
import com.kosta.dto.review.PageRequestDTO;
import com.kosta.dto.review.PageResponseDTO;
import com.kosta.dto.review.ReviewDTO;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ReviewService {
  
  ReviewDTO get(Long rno);
  
  Long register(ReviewDTO rno);
  
  PageResponseDTO<ReviewDTO> getList(PageRequestDTO pageRequestDTO);
  
  default ReviewDTO entityToDTO(Review review) {
    if (review == null) return null;
    
    return ReviewDTO.builder()
        .tno(review.getRno())
        .title(review.getTitle())
        .content(review.getContent())
        .imageNumber(review.getImageNumber())
        .createdAt(review.getCreatedAt())
        .updatedAt(review.getUpdatedAt())
        .isDeleted(review.getIsDeleted())
        .confirmed(review.getConfirmed())
        .memberId(review.getMember().getId())
        .categoryId(review.getCategory().getId())
        .build();
  }
  
  default Review dtoToEntity(ReviewDTO reviewDTO) {
    if (reviewDTO == null) return null;
    
    
    Member member = Member.builder().id(reviewDTO.getMemberId()).build();
    Category category = Category.builder().id(reviewDTO.getCategoryId()).build();
    
    return Review.builder()
        .rno(reviewDTO.getTno())
        .title(reviewDTO.getTitle())
        .content(reviewDTO.getContent())
        .imageNumber(reviewDTO.getImageNumber())
        .isDeleted(reviewDTO.getIsDeleted())
        .confirmed(reviewDTO.getConfirmed())
        .member(member)
        .category(category)
        .build();
  }
}
