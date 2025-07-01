package com.kosta.controller.mypage;

import com.kosta.domain.member.Member;
import com.kosta.dto.member.MemberDTO;
import com.kosta.dto.member.ResponseDTO;
import com.kosta.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
@Slf4j
public class MyPageController {

    private final MemberService memberService;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // 비밀번호 암호화를 위한 인코더

    // 마이페이지 조회 API
    @GetMapping
    public ResponseEntity<?> getMypage(@AuthenticationPrincipal String userId) {
        try {
            log.info("마이페이지 요청: userId = {}", userId);

            // userId로 회원 정보 조회
            Member member = memberService.getById(Long.parseLong(userId));

            if (member != null) {
                MemberDTO responseMemberDTO = MemberDTO.builder()
                        .id(member.getId())
                        .memberId(member.getMemberId())
                        .memberName(member.getMemberName())
                        .memberEmail(member.getMemberEmail())
                        .memberGender(member.getMemberGender())  // 성별 추가
                        .memberBirth(member.getMemberBirth())    // 생년월일 추가
                        .memberAddr(member.getMemberAddr())      // 주소 추가
                        .memberZip(member.getMemberZip())        // 우편번호 추가
                        .role(member.getRole())
                        .build();

                return ResponseEntity.ok().body(responseMemberDTO);
            } else {
                ResponseDTO responseDTO = ResponseDTO.builder().error("User not found").build();
                return ResponseEntity.badRequest().body(responseDTO);
            }
        } catch (Exception e) {
            log.error("마이페이지 조회 오류: ", e);
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    // 회원 정보 수정 API
    @PatchMapping
    public ResponseEntity<?> updateMypage(@AuthenticationPrincipal String userId, @RequestBody MemberDTO memberDTO) {
        try {
            log.info("회원 정보 수정 요청: userId = {}, memberDTO = {}", userId, memberDTO);

            // 현재 로그인한 사용자 정보 조회
            Member existingMember = memberService.getById(Long.parseLong(userId));

            if (existingMember == null) {
                ResponseDTO responseDTO = ResponseDTO.builder().error("User not found").build();
                return ResponseEntity.badRequest().body(responseDTO);
            }

            // 회원 정보 업데이트
            Member updatedMember = memberService.updateMember(existingMember, memberDTO, passwordEncoder);

            MemberDTO responseMemberDTO = MemberDTO.builder()
                    .id(updatedMember.getId())
                    .memberId(updatedMember.getMemberId())
                    .memberName(updatedMember.getMemberName())
                    .memberEmail(updatedMember.getMemberEmail())
                    .memberGender(updatedMember.getMemberGender())  // 성별 추가
                    .memberBirth(updatedMember.getMemberBirth())    // 생년월일 추가
                    .memberAddr(updatedMember.getMemberAddr())      // 주소 추가
                    .memberZip(updatedMember.getMemberZip())        // 우편번호 추가
                    .role(updatedMember.getRole())
                    .build();

            return ResponseEntity.ok().body(responseMemberDTO);

        } catch (Exception e) {
            log.error("회원 정보 수정 오류: ", e);
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    // 회원 탈퇴 API
    @DeleteMapping
    public ResponseEntity<?> deleteMypage(@AuthenticationPrincipal String userId, @RequestBody MemberDTO memberDTO) {
        try {
            log.info("회원 탈퇴 요청: userId = {}", userId);

            // 현재 로그인한 사용자 정보 조회
            Member existingMember = memberService.getById(Long.parseLong(userId));

            if (existingMember == null) {
                ResponseDTO responseDTO = ResponseDTO.builder().error("User not found").build();
                return ResponseEntity.badRequest().body(responseDTO);
            }

            // 비밀번호 확인
            if (memberDTO.getPassword() == null
                    || !passwordEncoder.matches(memberDTO.getPassword(), existingMember.getMemberPassword())) {
                ResponseDTO responseDTO = ResponseDTO.builder().error("Invalid password").build();
                return ResponseEntity.badRequest().body(responseDTO);
            }

            // 회원 탈퇴 처리
            memberService.deleteMember(existingMember);

            // 성공 메시지 (error 필드가 아닌 message 필드 사용)
            ResponseDTO responseDTO = ResponseDTO.builder().error(null).build();
            return ResponseEntity.ok().body(responseDTO);

        } catch (Exception e) {
            log.error("회원 탈퇴 오류: ", e);
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}