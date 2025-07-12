package com.kosta.controller.review;

import com.kosta.dto.review.CategoryDTO;
import com.kosta.dto.review.PageRequestDTO;
import com.kosta.dto.review.PageResponseDTO;
import com.kosta.dto.review.ReviewDTO;
import com.kosta.service.review.CategoryService;
import com.kosta.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {
  
  private final ReviewService reviewService;
  private final CategoryService categoryService;
  
  
  @GetMapping("/review/list")
  public PageResponseDTO<ReviewDTO> list(PageRequestDTO pageRequestDTO) {
    log.info("list.............. " + pageRequestDTO);
    
    return reviewService.getList(pageRequestDTO);
  }
  
  @GetMapping("/review/{tno}")
  public ReviewDTO get(@PathVariable("tno") Long tno) {
    return reviewService.get(tno);
  }
  
  // 현재 로그인한 사용자의 후기 목록 조회 - URL 경로 수정
  @GetMapping("/review/my-reviews")
  public PageResponseDTO<ReviewDTO> getMyReviews(
      @AuthenticationPrincipal String userId,
      PageRequestDTO pageRequestDTO) {
    log.info("getMyReviews - userId: " + userId + ", pageRequestDTO: " + pageRequestDTO);
    
    return reviewService.getMyReviews(Long.parseLong(userId), pageRequestDTO);
  }
  
  @PostMapping("/review")
  public  Map<String, String> insert(@RequestBody ReviewDTO dto) {
    log.info(dto);
    System.out.println("받은 데이터 = " + dto);
    reviewService.register(dto);
    
    return Map.of("SUCCESS", "INSERT");
  }
  
  @PutMapping("/review/{tno}")
  public ResponseEntity<?> updateReview(@PathVariable Long tno,
                                        @RequestBody ReviewDTO reviewDTO) {
    log.info(reviewDTO);
    
    // 혹시 DTO 안에 있는 tno와 경로 tno가 다른지도 확인
    if (!tno.equals(reviewDTO.getTno())) {
      return ResponseEntity.badRequest().body("Path의 tno와 DTO의 tno가 다릅니다.");
    }
    
    reviewService.update(reviewDTO);
    
    return ResponseEntity.ok("리뷰 수정 완료");
  }
  
  
  @DeleteMapping("/review/{tno}")
  public Map<String, String> delete(@PathVariable("tno") Long tno) {
    reviewService.remove(tno);
    return Map.of("SUCCESS", "Delete");
  }
  
  @GetMapping("/category")
  public ResponseEntity<List<CategoryDTO>> getCategories() {
    List<CategoryDTO> categories = categoryService.getAllCategories();
    return ResponseEntity.ok(categories);
  }
}