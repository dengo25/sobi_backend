package com.kosta.service.member;

import com.kosta.domain.member.Member;
import com.kosta.dto.member.MemberDTO;
import com.kosta.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    
    // 회원 정보 조회
    @Transactional(readOnly = true)
    public MemberDTO getMemberInfo(String memberId) {
        Member member = memberRepository.findByMemberIdAndIsActive(memberId, "Y")
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));
        
        return modelMapper.map(member, MemberDTO.class);
    }
    
    // 회원 정보 수정
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