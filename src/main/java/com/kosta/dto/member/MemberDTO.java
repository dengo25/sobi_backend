package com.kosta.dto.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    
    private Long id;
    private String memberId;
    private String memberName;
    private String memberGender;
    private String memberEmail;
    private String memberBirth;
    private String memberAddr;
    private String memberZip;
    private LocalDateTime memberReg;
    private String isActive;
    private String role;
}