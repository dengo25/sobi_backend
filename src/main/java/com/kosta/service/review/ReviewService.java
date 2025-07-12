package com.kosta.service.review;

import com.kosta.domain.member.Member;
import com.kosta.domain.reivew.Category;
import com.kosta.domain.reivew.Review;
import com.kosta.dto.review.PageRequestDTO;
import com.kosta.dto.review.PageResponseDTO;
import com.kosta.dto.review.ReviewDTO;
import com.kosta.dto.review.ReviewImageDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ReviewService {
  
  ReviewDTO get(Long rno);
  
  Long register(ReviewDTO dto);
  
  void update(ReviewDTO dto);
  
  void remove(Long rno);
  
  //관리자
  Page<ReviewDTO> getReviewPage(Pageable pageable);
  
  PageResponseDTO<ReviewDTO> getList(PageRequestDTO pageRequestDTO);
  
  // 현재 로그인한 사용자의 후기 목록 조회
  PageResponseDTO<ReviewDTO> getMyReviews(Long memberId, PageRequestDTO pageRequestDTO);
  
  
  
  default ReviewDTO entityToDTO(Review review) {
    if (review == null) return null;
    
    // ReviewImage → ReviewImageDTO 변환
    List<ReviewImageDTO> imageDTOs = null;
    if (review.getImages() != null && !review.getImages().isEmpty()) {
      imageDTOs = review.getImages().stream()
          .map(img -> ReviewImageDTO.builder()
              .ino(img.getIno())
              .reviewId(review.getRno()) // 리뷰 ID도 설정
              .fileUrl(img.getFileUrl())
              .originalFileName(img.getOriginalFileName())
              .fileType(img.getFileType())
              .isThumbnail(img.getIsThumbnail())
              .build())
          .toList();
    }
    
    return ReviewDTO.builder()
        .tno(review.getRno())
        .title(review.getTitle())
        .content(review.getContent())
        .imageNumber(review.getImageNumber())
        .createdAt(review.getCreatedAt())
        .updatedAt(review.getUpdatedAt())
        .isDeleted(review.getIsDeleted())
        .confirmed(review.getConfirmed())
        .memberId(review.getMember().getMemberId())
        .categoryId(review.getCategory().getId())
        .images(imageDTOs) // 전체 이미지 리스트 포함
        .build();
  }
  
  default Review dtoToEntity(ReviewDTO reviewDTO) {
    if (reviewDTO == null) return null;
    
    
    Member member = Member.builder().memberId(reviewDTO.getMemberId()).build();
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