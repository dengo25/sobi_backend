package com.kosta.service.review;

import com.kosta.domain.member.Member;
import com.kosta.domain.review.Category;
import com.kosta.domain.review.Review;
import com.kosta.dto.review.PageRequestDTO;
import com.kosta.dto.review.PageResponseDTO;
import com.kosta.dto.review.ReviewDTO;
import com.kosta.dto.review.CategoryDTO;
import com.kosta.dto.review.ReviewImageDTO;
import com.kosta.repository.member.MemberRepository;
import com.kosta.repository.review.CategoryRepository;
import com.kosta.repository.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{
  private final ReviewRepository reviewRepository;
  private final MemberRepository memberRepository;
  private final CategoryRepository categoryRepository;
  
  @Override
  public ReviewDTO get(Long rno) {
    Optional<Review> result = reviewRepository.findByRno(rno);
    Review review = result.orElseThrow();
    return entityToDTO(review);
  }
  
  @Override
  public Long register(ReviewDTO dto) {
    Review review = dtoToEntity(dto);
    Member member = memberRepository.findByMemberId(dto.getMemberId());
    review.setMember(member);
    
    if(member == null){
      throw new RuntimeException("존재하지 않는 회원 입니다!");
    }
    
    if (dto.getImages() != null && !dto.getImages().isEmpty()) {
      dto.getImages().forEach(imgDTO -> {
        review.addImage(
            imgDTO.getFileUrl(),
            imgDTO.getOriginalFileName(),
            imgDTO.getFileType(),
            imgDTO.getIsThumbnail()
        );
      });
    }
    
    Review result = reviewRepository.save(review);
    return result.getRno();
  }
  
  @Override
  public void update(ReviewDTO dto) {
    log.info("리뷰 수정 시작 - DTO: {}", dto);
    
    // 기존 리뷰 조회
    Optional<Review> result = reviewRepository.findById(dto.getTno());
    Review review = result.orElseThrow(() -> new RuntimeException("리뷰가 존재하지 않습니다."));
    
    // 회원 조회
    Member member = memberRepository.findByMemberId(dto.getMemberId());
    if (member == null) {
      throw new RuntimeException("존재하지 않는 회원입니다.");
    }
    
    // 카테고리 조회
    Optional<Category> categoryOpt = categoryRepository.findById(dto.getCategoryId());
    Category category = categoryOpt.orElseThrow(() -> new RuntimeException("존재하지 않는 카테고리입니다."));
    
    // 리뷰 정보 업데이트
    review.setTitle(dto.getTitle());
    review.setContent(dto.getContent());
    review.setCategory(category);
    review.setMember(member);
    
    // 기존 이미지 모두 삭제
    review.clearImages();
    
    // 새 이미지 추가
    if (dto.getImages() != null && !dto.getImages().isEmpty()) {
      dto.getImages().forEach(imgDTO -> {
        review.addImage(
            imgDTO.getFileUrl(),
            imgDTO.getOriginalFileName(),
            imgDTO.getFileType(),
            imgDTO.getIsThumbnail()
        );
      });
      review.setImageNumber((long) dto.getImages().size());
    } else {
      review.setImageNumber(0L);
    }
    
    // 저장
    reviewRepository.save(review);
    log.info("리뷰 수정 완료 - tno: {}", dto.getTno());
  }

  @Override
  public void remove(Long rno) {
    Optional<Review> result = reviewRepository.findById(rno);
    Review review = result.orElseThrow(() -> new RuntimeException("삭제할 리뷰가 존재하지 않습니다."));
    
    // 이미 삭제된 리뷰인지 확인
    if ("Y".equals(review.getIsDeleted())) {
      throw new RuntimeException("이미 삭제된 리뷰입니다.");
    }
    
    // 논리적 삭제 수행
    review.setIsDeleted("Y");
    reviewRepository.save(review);
    
    log.info("리뷰 논리적 삭제 완료 - rno: {}", rno);
  }
  
  @Override
  public PageResponseDTO<ReviewDTO> getList(PageRequestDTO pageRequestDTO) {
    //JPA
    Page<Review> result = reviewRepository.search1(pageRequestDTO);
    
    //갖고 온건 entity List이지만 reviewDTO List가 되어야 한다.
    //review를 dto로 변경하고 컬렉션으로 묶는다.
    List<ReviewDTO> rnoList = result.get().map(review -> entityToDTO(review)).collect(Collectors.toList());
    
    PageResponseDTO<ReviewDTO> responseDTO =
        PageResponseDTO.<ReviewDTO>withAll() //BuilderMethod에서 정한 이름 WithAll
        .rnoList(rnoList)
        .pageRequestDTO(pageRequestDTO)
        .totalCount(result.getTotalElements())
        .build();
    
    return responseDTO;
  }
  
  @Override
  public PageResponseDTO<ReviewDTO> getMyReviews(Long memberId, PageRequestDTO pageRequestDTO) {
    log.info("getMyReviews - memberId: " + memberId);
    
    List<Review> reviews = reviewRepository.findByMemberIdOrderByCreatedAtDesc(memberId);
    
    List<ReviewDTO> rnoList = reviews.stream()
        .map(review -> entityToDTO(review))
        .collect(Collectors.toList());
    
    PageResponseDTO<ReviewDTO> responseDTO =
        PageResponseDTO.<ReviewDTO>withAll()
        .rnoList(rnoList)
        .pageRequestDTO(pageRequestDTO)
        .totalCount(reviews.size())
        .build();
    
    return responseDTO;
  }
  
  @Override
  public ReviewDTO entityToDTO(Review review) {
    if (review == null) return null;
    
    List<ReviewImageDTO> imageDTOs = null;
    if (review.getImages() != null && !review.getImages().isEmpty()) {
      imageDTOs = review.getImages().stream()
          .map(img -> ReviewImageDTO.builder()
              .ino(img.getIno())
              .reviewId(review.getRno())
              .fileUrl(img.getFileUrl())
              .originalFileName(img.getOriginalFileName())
              .fileType(img.getFileType())
              .isThumbnail(img.getIsThumbnail())
              .build())
          .toList();
    }
    
    CategoryDTO categoryDTO = null;
    if (review.getCategory() != null) {
      categoryDTO = CategoryDTO.builder()
          .id(review.getCategory().getId())
          .name(review.getCategory().getName())
          .build();
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
        .categoryId(review.getCategory() != null ? review.getCategory().getId() : null)
        .category(categoryDTO) 
        .images(imageDTOs)
        .build();
  }
}