package com.kosta.controller.member;

import com.kosta.domain.member.Member;
import com.kosta.dto.member.MemberDTO;
import com.kosta.dto.member.ResponseDTO;
import com.kosta.security.TokenProvider;
import com.kosta.service.blacklist.BlacklistService;
import com.kosta.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
  
  private final MemberService memberService;
  private final TokenProvider tokenProvider; //JWT토큰 생성 유틸
  private final BlacklistService blacklistService;
  
  private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); //비밀번호 암호화를 위한 인코더
  
  @PostMapping("/signup")
  public ResponseEntity<?> registerMember(@RequestBody MemberDTO memberDTO) {
    System.out.println(memberDTO +"membeDTO넘어옴");
    try {
      if (memberDTO == null || memberDTO.getPassword() == null) {
        throw new RuntimeException("Invalid Password value.");
      }
      if (blacklistService.isBlacklisted(memberDTO.getMemberName())) {
          throw new RuntimeException("해당 계정은 이용이 제한되어 있습니다. 관리자에게 문의해주세요.");
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
  
  @PostMapping("/login")
  public ResponseEntity<?> authenticate(@RequestBody MemberDTO memberDTO) {
    log.info("로그인 요청: {}", memberDTO);
    try {
        if (blacklistService.isBlacklisted(memberDTO.getMemberId())) {
            throw new RuntimeException("해당 계정은 이용이 제한되어 있습니다. 관리자에게 문의해주세요.");
        }
        Member member = memberService.getByCredentials(memberDTO.getMemberId(), memberDTO.getPassword(), passwordEncoder);
        
        if (member != null) {
          String token = tokenProvider.create(member);
          
          MemberDTO responseMemberDTO = memberDTO.builder()
              .memberName(member.getMemberName())
              .id(member.getId())
              .memberId(member.getMemberId())
              .memberEmail(member.getMemberEmail())
              .token(token)
              .role(member.getRole())
              .build();
          
          log.info("로그인 응답 DTO: {}", responseMemberDTO);
          return ResponseEntity.ok().body(responseMemberDTO);
        }
        else {
          ResponseDTO responseDTO = ResponseDTO.builder()
              .error("Login failed")
              .build();
          return ResponseEntity.badRequest().body(responseDTO);
        }
	} catch (Exception e) {
        ResponseDTO responseDTO = ResponseDTO.builder()
                .error(e.getMessage())
                .build();
            return ResponseEntity.badRequest().body(responseDTO);
	}

  }
  
  /**
   * 이메일 중복 확인 API
   * @param email 확인할 이메일 주소
   * @return 사용 가능 여부와 메시지
   */
  @GetMapping("/check-email")
  public ResponseEntity<?> checkEmailDuplicate(@RequestParam("email") String email) {
    log.info("이메일 중복 확인 요청: {}", email);
    
    try {
      // 이메일 형식 간단 검증
      if (email == null || email.trim().isEmpty()) {
        Map<String, Object> response = new HashMap<>();
        response.put("available", false);
        response.put("message", "이메일을 입력해주세요.");
        return ResponseEntity.badRequest().body(response);
      }
      
      // 기본 이메일 형식 검증
      if (!email.contains("@") || !email.contains(".")) {
        Map<String, Object> response = new HashMap<>();
        response.put("available", false);
        response.put("message", "올바른 이메일 형식이 아닙니다.");
        return ResponseEntity.badRequest().body(response);
      }
      
      boolean isAvailable = memberService.isEmailAvailable(email);
      
      Map<String, Object> response = new HashMap<>();
      response.put("available", isAvailable);
      response.put("message", isAvailable ? "사용 가능한 이메일입니다." : "이미 사용 중인 이메일입니다.");
      
      log.info("이메일 중복 확인 결과: {} - {}", email, isAvailable);
      return ResponseEntity.ok().body(response);
      
    } catch (Exception e) {
      log.error("이메일 중복 확인 중 오류 발생: {}", e.getMessage(), e);
      
      Map<String, Object> response = new HashMap<>();
      response.put("available", false);
      response.put("message", "이메일 확인 중 오류가 발생했습니다.");
      return ResponseEntity.status(500).body(response);
    }
  }
}