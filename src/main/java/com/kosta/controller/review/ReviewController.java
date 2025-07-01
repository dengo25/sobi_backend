package com.kosta.controller.review;

import com.kosta.dto.review.PageRequestDTO;
import com.kosta.dto.review.PageResponseDTO;
import com.kosta.dto.review.ReviewDTO;
import com.kosta.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {
  
  private final ReviewService reviewService;
  
  @GetMapping("/list")
  public PageResponseDTO<ReviewDTO> list(PageRequestDTO pageRequestDTO) {
    log.info("list.............. " + pageRequestDTO);
    
    return reviewService.getList(pageRequestDTO);
  }
}
