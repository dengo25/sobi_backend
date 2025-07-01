package com.kosta.service.member;

import com.kosta.domain.member.Member;
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
    
    //같은 사용자가 있는지 확인
    if (memberRepository.existsByMemberId(memberId)) {
      throw new RuntimeException("Member Id already exists");
    }
    return memberRepository.save(member);
  }
  
  
  public Member getByCredentials(String memberId, String password, PasswordEncoder encoder) {
    
    Member originMember = memberRepository.findByMemberId(memberId);
    
    //사용자 존재 및 비밀번호 일치 여부 확인
    if (originMember != null && encoder.matches(password, originMember.getMemberPassword())) {
      return originMember;
    }
    return null;
  }
}
