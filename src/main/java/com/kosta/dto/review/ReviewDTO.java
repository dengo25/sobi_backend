package com.kosta.dto.review;

import com.kosta.domain.member.Member;
import com.kosta.domain.reivew.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
  
  private Integer id;
  
  private String title;
  
  
  private String content;
  
  private Integer imageNumber;
  
  private LocalDateTime createdAt;
  
  private LocalDateTime updatedAt;
  
  private String isDeleted;
  
  private String confirmed;
  
  // DTO를 만들 때 객체를 포함하지 않고 필요한 정보만 정의한다.
  private Long memberId;
  
  private Integer categoryId;
  
}
