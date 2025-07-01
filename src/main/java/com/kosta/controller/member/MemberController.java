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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    
    private final MemberService memberService;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    // 회원가입 (from member-ready branch)
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody MemberDTO memberDTO) {
        log.info("회원가입 요청: {}", memberDTO);
        try {
            if (memberDTO == null || memberDTO.getPassword() == null) {
                throw new RuntimeException("Invalid Password value.");
            }
            
            Member member = Member.builder()
                    .memberId(memberDTO.getMemberId())
                    .memberName(memberDTO.getMemberName())
                    .memberPassword(passwordEncoder.encode(memberDTO.getPassword()))
                    .memberEmail(memberDTO.getMemberEmail())
                    .memberGender(memberDTO.getMemberGender())
                    .memberBirth(memberDTO.getMemberBirth())
                    .memberAddr(memberDTO.getMemberAddr())
                    .memberZip(memberDTO.getMemberZip())
                    .build();
            
            Member registeredMember = memberService.create(member);
            MemberDTO responseMemberDTO = MemberDTO.builder()
                    .id(registeredMember.getId())
                    .memberId(registeredMember.getMemberId())
                    .memberName(registeredMember.getMemberName())
                    .memberEmail(registeredMember.getMemberEmail())
                    .role(registeredMember.getRole())
                    .build();
            
            return ResponseEntity.ok().body(responseMemberDTO);
        } catch (Exception e) {
            log.error("회원가입 실패: {}", e.getMessage());
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
    
    // 로그인 (from member-ready branch)
    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody MemberDTO memberDTO) {
        log.info("로그인 요청: {}", memberDTO.getMemberId());
        try {
            Member member = memberService.getByCredentials(
                    memberDTO.getMemberId(), 
                    memberDTO.getPassword(), 
                    passwordEncoder
            );
            
            if (member != null) {
                String token = tokenProvider.create(member);
                
                MemberDTO responseMemberDTO = MemberDTO.builder()
                        .id(member.getId())
                        .memberId(member.getMemberId())
                        .memberName(member.getMemberName())
                        .memberEmail(member.getMemberEmail())
                        .role(member.getRole())
                        .token(token)
                        .build();
                
                return ResponseEntity.ok().body(responseMemberDTO);
            } else {
                ResponseDTO responseDTO = ResponseDTO.builder()
                        .error("Login failed")
                        .build();
                return ResponseEntity.badRequest().body(responseDTO);
            }
        } catch (Exception e) {
            log.error("로그인 실패: {}", e.getMessage());
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("Login failed")
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
    
    // 회원 정보 조회 (from HEAD branch - mypage functionality)
    @GetMapping("/mypage/{memberId}")
    public ResponseEntity<MemberDTO> getMemberInfo(@PathVariable String memberId) {
        try {
            MemberDTO memberDTO = memberService.getMemberInfo(memberId);
            return ResponseEntity.ok(memberDTO);
        } catch (Exception e) {
            log.error("회원 정보 조회 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    // 회원 정보 수정 (from HEAD branch - mypage functionality)
    @PutMapping("/mypage/{memberId}")
    public ResponseEntity<MemberDTO> updateMemberInfo(
            @PathVariable String memberId, 
            @RequestBody MemberDTO memberDTO) {
        try {
            MemberDTO updatedMember = memberService.updateMemberInfo(memberId, memberDTO);
            return ResponseEntity.ok(updatedMember);
        } catch (Exception e) {
            log.error("회원 정보 수정 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}