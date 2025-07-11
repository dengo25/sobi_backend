package com.kosta.service.review;

import com.kosta.domain.member.Member;
import com.kosta.domain.reivew.Review;
import com.kosta.dto.review.PageRequestDTO;
import com.kosta.dto.review.PageResponseDTO;
import com.kosta.dto.review.ReviewDTO;
import com.kosta.repository.member.MemberRepository;
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
    Optional<Review> result = reviewRepository.findById(dto.getTno());
    Review review = result.orElseThrow(() -> new RuntimeException("리뷰가 존재하지 않습니다."));
    
    review.setTitle(dto.getTitle());
    review.setContent(dto.getContent());
    review.setImageNumber(dto.getImageNumber());
    
    review.clearImages();
    
    if (dto.getImages() != null && !dto.getImages().isEmpty()) {
      dto.getImages().forEach(imgDTO -> {
        review.addImage(
            imgDTO.getFileUrl(),
            imgDTO.getOriginalFileName(),
            imgDTO.getFileType(),
            imgDTO.getIsThumbnail()
        );
      });
    } //end if
    reviewRepository.save(review);
    
  }

  
  @Override
  public void remove(Long rno) {
    reviewRepository.deleteById(rno);
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
    
    // 특정 회원의 모든 후기 조회 (페이징 없이)
    List<Review> reviews = reviewRepository.findByMemberIdOrderByCreatedAtDesc(memberId);
    
    // Entity를 DTO로 변환
    List<ReviewDTO> rnoList = reviews.stream()
        .map(review -> entityToDTO(review))
        .collect(Collectors.toList());
    
    // 간단한 응답 객체 생성 (페이징 정보는 기본값 사용)
    PageResponseDTO<ReviewDTO> responseDTO =
        PageResponseDTO.<ReviewDTO>withAll()
        .rnoList(rnoList)
        .pageRequestDTO(pageRequestDTO)
        .totalCount(reviews.size())
        .build();
    
    return responseDTO;
  }
}