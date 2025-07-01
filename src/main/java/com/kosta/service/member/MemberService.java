package com.kosta.service.member;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kosta.domain.member.Member;
import com.kosta.dto.member.MemberDTO;
import com.kosta.repository.member.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    
    // 회원 생성 (from member-ready branch)
    public Member create(Member member) {
        if (member == null || member.getMemberId() == null) {
            throw new RuntimeException("Invalid Arguments");
        }
        
        String memberId = member.getMemberId(); // 사용자명 추출
        
        // 같은 사용자가 있는지 확인
        if (memberRepository.existsByMemberId(memberId)) {
            throw new RuntimeException("Member Id already exists");
        }
        return memberRepository.save(member);
    }
    
    // 인증 처리 (from member-ready branch)
    public Member getByCredentials(String memberId, String password, PasswordEncoder encoder) {
        Member originMember = memberRepository.findByMemberId(memberId);
        
        // 사용자 존재 및 비밀번호 일치 여부 확인
        if (originMember != null && encoder.matches(password, originMember.getMemberPassword())) {
            return originMember;
        }
        return null;
    }
    
    // 회원 정보 조회 (from HEAD)
    @Transactional(readOnly = true)
    public MemberDTO getMemberInfo(String memberId) {
        Member member = memberRepository.findByMemberIdAndIsActive(memberId, "Y")
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));
        
        return modelMapper.map(member, MemberDTO.class);
    }
    
    // 회원 정보 수정 (from HEAD)
    public MemberDTO updateMemberInfo(String memberId, MemberDTO memberDTO) {
        Member member = memberRepository.findByMemberIdAndIsActive(memberId, "Y")
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));
        
        // 수정 가능한 필드들만 업데이트
        if (memberDTO.getMemberName() != null) {
            member.setMemberName(memberDTO.getMemberName());
        }
        if (memberDTO.getMemberEmail() != null) {
            member.setMemberEmail(memberDTO.getMemberEmail());
        }
        if (memberDTO.getMemberGender() != null) {
            member.setMemberGender(memberDTO.getMemberGender());
        }
        if (memberDTO.getMemberBirth() != null) {
            member.setMemberBirth(memberDTO.getMemberBirth());
        }
        if (memberDTO.getMemberAddr() != null) {
            member.setMemberAddr(memberDTO.getMemberAddr());
        }
        if (memberDTO.getMemberZip() != null) {
            member.setMemberZip(memberDTO.getMemberZip());
        }
        
        Member savedMember = memberRepository.save(member);
        return modelMapper.map(savedMember, MemberDTO.class);
    }
}
