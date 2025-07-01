package com.kosta.dto.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    
    // Authentication fields
    private String token;
    
    // Basic member information
    private Long id;
    private String memberId;
    
    @JsonIgnore // Prevent password from being serialized in responses
    private String password;
    
    private String memberName;
    private String memberEmail;
    
    // Additional member details (from HEAD branch)
    private String memberGender;
    private String memberBirth;
    private String memberAddr;
    private String memberZip;
    private LocalDateTime memberReg;
    
    // Status and role
    private String isActive;
    private String role;
}