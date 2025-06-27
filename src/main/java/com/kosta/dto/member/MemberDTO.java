package com.kosta.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDTO {
  
  private String token;
  
  private String memberName;
  
  private String password;
  
  private Long id;
}
