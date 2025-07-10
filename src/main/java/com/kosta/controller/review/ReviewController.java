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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
  
  @GetMapping("/category")
  public ResponseEntity<List<CategoryDTO>> getCategories() {
    List<CategoryDTO> categories = categoryService.getAllCategories();
    return ResponseEntity.ok(categories);
  }
}