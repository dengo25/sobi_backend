package com.kosta.dto.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder //상속을 해야될 경우가 생길수도 있어서
public class PageRequestDTO {
  @Builder.Default
  private int page = 1;  //페이지 번호가 없으면 1
  
  @Builder.Default
  private int size = 10;
  
  //카테고리별 조회시
  private Long category;
}
