package com.kosta.controller.member;

import com.kosta.dto.member.MemberDTO;
import com.kosta.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mypage")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class MemberController {
    
    private final MemberService memberService;
    
    // 회원 정보 조회
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberDTO> getMemberInfo(@PathVariable String memberId) {
        try {
            MemberDTO memberDTO = memberService.getMemberInfo(memberId);
            return ResponseEntity.ok(memberDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // 회원 정보 수정
    @PutMapping("/{memberId}")
    public ResponseEntity<MemberDTO> updateMemberInfo(
            @PathVariable String memberId, 
            @RequestBody MemberDTO memberDTO) {
        try {
            MemberDTO updatedMember = memberService.updateMemberInfo(memberId, memberDTO);
            return ResponseEntity.ok(updatedMember);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}