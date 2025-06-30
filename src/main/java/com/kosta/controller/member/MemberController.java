package com.kosta.controller.member;

import com.kosta.domain.member.Member;
import com.kosta.dto.member.MemberDTO;
import com.kosta.dto.member.ResponseDTO;
import com.kosta.security.TokenProvider;
import com.kosta.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
  
  private final MemberService memberService;
  private final TokenProvider tokenProvider; //JWT토큰 생성 유틸
  
  private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); //비밀번호 암호화를 위한 인코더
  
  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@RequestBody MemberDTO memberDTO) {
    System.out.println(memberDTO +"membeDTO넘어옴");
    try {
      if (memberDTO == null || memberDTO.getPassword() == null) {
        throw new RuntimeException("Invalid Password value.");
      }
      
      Member member = Member.builder()
          .memberId(memberDTO.getMemberId())
          .memberName(memberDTO.getMemberName())
          .memberPassword(passwordEncoder.encode(memberDTO.getPassword()))
          .memberEmail(memberDTO.getMemberEmail())
          .memberAddr(memberDTO.getMemberAddr())
          .memberZip(memberDTO.getMemberZip())
          .build();
      
      
      Member registeredMember = memberService.create(member);
      MemberDTO responseMemberDTO = MemberDTO.builder()
          .id(registeredMember.getId())
          .memberName(registeredMember.getMemberName())
          .memberEmail(registeredMember.getMemberEmail())
          .memberAddr(registeredMember.getMemberAddr())
          .memberZip(registeredMember.getMemberZip())
          .build();
      
      return ResponseEntity.ok().body(responseMemberDTO);
    }
    catch (Exception e) {
      ResponseDTO responseDTO = ResponseDTO.builder()
          .error(e.getMessage())
          .build();
      
      return ResponseEntity.badRequest().body(responseDTO);
    }
  }
}
