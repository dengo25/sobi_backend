package com.kosta.service.member;

import com.kosta.domain.member.Member;
import com.kosta.dto.member.MemberDTO;
import com.kosta.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class MemberService {
  
  private final MemberRepository memberRepository;
  
  public Member create(Member member) {
    if (member == null || member.getMemberId() == null) {
      throw new RuntimeException("Invalid Arguments");
    }
    
    String memberId = member.getMemberId(); // 사용자명 추출
    
    // 활성 사용자 중 같은 사용자가 있는지 확인
    if (memberRepository.existsByMemberIdAndIsActive(memberId, "Y")) {
      throw new RuntimeException("Member Id already exists");
    }
    return memberRepository.save(member);
  }
  
  public Member getByCredentials(String memberId, String password, PasswordEncoder encoder) {
    
    // 활성 상태인 사용자만 조회
    Member originMember = memberRepository.findByMemberIdAndIsActive(memberId, "Y");
    
    // 사용자 존재 및 비밀번호 일치 여부 확인
    if (originMember != null && encoder.matches(password, originMember.getMemberPassword())) {
      return originMember;
    }
    return null;
  }
  
  // 마이페이지용 회원 조회
  public Member getById(Long id) {
    return memberRepository.findByIdAndIsActive(id, "Y").orElse(null);
  }
  
  // 회원 정보 수정 
  public Member updateMember(Member existingMember, MemberDTO memberDTO, PasswordEncoder encoder) {
    if (memberDTO.getMemberName() != null && !memberDTO.getMemberName().trim().isEmpty()) {
      existingMember.setMemberName(memberDTO.getMemberName());
    }
    
    if (memberDTO.getMemberEmail() != null && !memberDTO.getMemberEmail().trim().isEmpty()) {
      existingMember.setMemberEmail(memberDTO.getMemberEmail());
    }
    
    if (memberDTO.getMemberGender() != null && !memberDTO.getMemberGender().trim().isEmpty()) {
      existingMember.setMemberGender(memberDTO.getMemberGender());
    }
    
    if (memberDTO.getMemberBirth() != null && !memberDTO.getMemberBirth().trim().isEmpty()) {
      existingMember.setMemberBirth(memberDTO.getMemberBirth());
    }
    
    if (memberDTO.getMemberAddr() != null && !memberDTO.getMemberAddr().trim().isEmpty()) {
      existingMember.setMemberAddr(memberDTO.getMemberAddr());
    }
    
    if (memberDTO.getMemberZip() != null && !memberDTO.getMemberZip().trim().isEmpty()) {
      existingMember.setMemberZip(memberDTO.getMemberZip());
    }
    
    if (memberDTO.getPassword() != null && !memberDTO.getPassword().trim().isEmpty()) {
      existingMember.setMemberPassword(encoder.encode(memberDTO.getPassword()));
    }
    
    return memberRepository.save(existingMember);
  }
  
  // 회원 탈퇴 메서드 - 논리적 삭제
  public void deleteMember(Member member) {
    log.info("논리적 삭제 처리: memberId = {}, id = {}", member.getMemberId(), member.getId());
    
    // 물리적 삭제 대신 isActive를 'N'으로 변경
    member.setIsActive("N");
    memberRepository.save(member);
    
    log.info("논리적 삭제 완료: memberId = {}", member.getMemberId());
  }
  
  /**
   * 이메일 사용 가능 여부 확인
   * @param email 확인할 이메일 주소
   * @return true: 사용 가능, false: 이미 사용 중
   */
  public boolean isEmailAvailable(String email) {
    try {
      log.info("이메일 중복 확인: {}", email);
      
      // 활성 회원 중에서 해당 이메일이 존재하는지 확인
      boolean exists = memberRepository.existsByMemberEmailAndIsActive(email, "Y");
      
      log.info("이메일 {} 사용 가능 여부: {}", email, !exists);
      return !exists; // 존재하지 않으면 사용 가능
      
    } catch (Exception e) {
      log.error("이메일 중복 확인 중 데이터베이스 오류 발생: {}", e.getMessage(), e);
      throw new RuntimeException("이메일 중복 확인 중 오류가 발생했습니다.", e);
    }
  }
}