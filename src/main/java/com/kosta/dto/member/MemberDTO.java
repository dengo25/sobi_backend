package com.kosta.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
  private String token;
  private Long id;
  private String memberId;
  private String password; //응답에 사용시 jsonignore등으로 처리해야
  
  private String memberAddr;
  private String memberZip;
  private String memberName;
  private String memberEmail;
  private String role;
}