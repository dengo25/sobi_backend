package com.kosta.dto.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
  
  private Long tno;
  
  private String title;
  
  
  private String content;
  
  private Long imageNumber;
  
  private LocalDateTime createdAt;
  
  private LocalDateTime updatedAt;
  
  private String isDeleted;
  
  private String confirmed;
  
  // DTO를 만들 때 객체를 포함하지 않고 필요한 정보만 정의한다.
  private String memberId;
  
  private Long categoryId;
  
  
  private List<ReviewImageDTO> images;
}
