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
  
  private String memberId;
  
  private Long categoryId;
  
  private CategoryDTO category;
  
  private List<ReviewImageDTO> images;
}