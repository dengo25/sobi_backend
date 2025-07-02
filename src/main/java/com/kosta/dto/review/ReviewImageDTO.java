package com.kosta.dto.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewImageDTO {
  
  private Long ino;
  
  private Long reviewId;
  
  private String fileUrl;
  
  private String originalFileName;
  
  private String fileType;
  
  private String isThumbnail;
}