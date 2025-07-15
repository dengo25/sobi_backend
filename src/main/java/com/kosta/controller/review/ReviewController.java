package com.kosta.controller.review;

import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.kosta.dto.review.CategoryDTO;
import com.kosta.dto.review.PageRequestDTO;
import com.kosta.dto.review.PageResponseDTO;
import com.kosta.dto.review.ReviewDTO;
import com.kosta.security.TokenProvider;
import com.kosta.service.review.CategoryService;
import com.kosta.service.review.ReviewService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

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
  public ReviewDTO get(@PathVariable("tno") Long tno) { // 🔥 명시적으로 "tno" 지정
    return reviewService.get(tno);
  }
  
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
  public ResponseEntity<?> updateReview(@PathVariable("tno") Long tno,
                                        @RequestBody ReviewDTO reviewDTO,
                                        @AuthenticationPrincipal String userId) {
    log.info("수정 요청 - tno: {}, reviewDTO: {}", tno, reviewDTO);
    
    try {
      ReviewDTO existingReview = reviewService.get(tno);
      
      String token = getTokenFromRequest();
      if (token != null) {
        TokenProvider tokenProvider = new TokenProvider();
        String memberIdFromToken = tokenProvider.getMemberIdFromToken(token);
        
        if (!existingReview.getMemberId().equals(memberIdFromToken)) {
          return ResponseEntity.status(403)
              .body(Map.of("error", "본인이 작성한 리뷰만 수정할 수 있습니다."));
        }
        
        reviewDTO.setTno(tno);
        reviewDTO.setMemberId(memberIdFromToken);
        
      } else {
        return ResponseEntity.status(401)
            .body(Map.of("error", "인증이 필요합니다."));
      }
      
      if (reviewDTO.getCategoryId() == null) {
        return ResponseEntity.badRequest()
            .body(Map.of("error", "카테고리를 선택해주세요."));
      }
      
      reviewService.update(reviewDTO);
      log.info("리뷰 수정 완료 - tno: {}", tno);
      return ResponseEntity.ok(Map.of("SUCCESS", "UPDATE", "message", "리뷰 수정 완료"));
      
    } catch (RuntimeException e) {
      log.error("리뷰 수정 실패 - tno: {}, error: {}", tno, e.getMessage(), e);
      return ResponseEntity.badRequest()
          .body(Map.of("error", e.getMessage()));
    } catch (Exception e) {
      log.error("리뷰 수정 중 예외 발생 - tno: {}, error: {}", tno, e.getMessage(), e);
      return ResponseEntity.status(500)
          .body(Map.of("error", "리뷰 수정 중 오류가 발생했습니다: " + e.getMessage()));
    }
  }
  
  
  @DeleteMapping("/review/{tno}")
  public ResponseEntity<?> delete(@PathVariable("tno") Long tno, // 🔥 명시적으로 "tno" 지정
                                 @AuthenticationPrincipal String userId) {
    try {
      ReviewDTO currentReview = reviewService.get(tno);
      
      String token = getTokenFromRequest();
      if (token != null) {
        TokenProvider tokenProvider = new TokenProvider();
        String memberIdFromToken = tokenProvider.getMemberIdFromToken(token);
        
        if (!currentReview.getMemberId().equals(memberIdFromToken)) {
          return ResponseEntity.status(HttpStatus.SC_FORBIDDEN)
              .body(Map.of("error", "본인이 작성한 리뷰만 삭제할 수 있습니다."));
        }
      } else {
        return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED)
            .body(Map.of("error", "인증이 필요합니다."));
      }
      
      reviewService.remove(tno);
      
      return ResponseEntity.ok(Map.of("SUCCESS", "Delete", "message", "리뷰가 성공적으로 삭제되었습니다."));
      
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest()
          .body(Map.of("error", e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
          .body(Map.of("error", "서버 오류가 발생했습니다."));
    }
  }

  private String getTokenFromRequest() {
	  HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
	  String bearerToken = request.getHeader("Authorization");
	  if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
	    return bearerToken.substring(7);
	  }
	  return null;
	}
  
  @GetMapping("/category")
  public ResponseEntity<List<CategoryDTO>> getCategories() {
    List<CategoryDTO> categories = categoryService.getAllCategories();
    return ResponseEntity.ok(categories);
  }
  
  @GetMapping("/health")
  public String healthCheck() {
    return "OK";
  }
}