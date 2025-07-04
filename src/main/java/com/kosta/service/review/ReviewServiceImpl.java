package com.kosta.service.review;

import com.kosta.domain.reivew.Review;
import com.kosta.dto.review.PageRequestDTO;
import com.kosta.dto.review.PageResponseDTO;
import com.kosta.dto.review.ReviewDTO;
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
  
  @Override
  public ReviewDTO get(Long rno) {
    Optional<Review> result = reviewRepository.findByRno(rno);
    Review review = result.orElseThrow();
    return entityToDTO(review);
  }
  
  
  
  @Override
  public Long register(ReviewDTO rno) {
    
    Review review = dtoToEntity(rno);
    Review result = reviewRepository.save(review);
    return result.getRno();
  }
  
  @Override
  public void modify(ReviewDTO dto) { //원래 엔티티를 가져와서 처리하기 떄문에 주의
    Optional<Review> result = reviewRepository.findById(dto.getTno());
    Review review = result.orElseThrow();
    review.setTitle(dto.getTitle());
    review.setContent(dto.getContent());
    review.setImageNumber(dto.getImageNumber());
    
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
  
}
