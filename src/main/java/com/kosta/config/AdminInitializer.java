package com.kosta.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.kosta.domain.member.Member;
import com.kosta.repository.member.MemberRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminInitializer {

    private final MemberRepository memberRepository;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostConstruct //실행될 때 자동으로 한 번 실행
    public void init() {
        if (!memberRepository.existsByMemberId("admin")) {
            Member admin = new Member();
            admin.setMemberId("admin");
            admin.setMemberPassword(passwordEncoder.encode("admin1234"));
            admin.setRole("ROLE_ADMIN");
            admin.setMemberName("ADMIN");
            memberRepository.save(admin);
        }
    }
}
