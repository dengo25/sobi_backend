package com.kosta.domain.member;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MEMBER")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString(exclude = {
    "socialLinks", // Lazy 예외 방지
    "memberGender",
    "memberBirth",
    "memberAddr",
    "memberZip",
    "memberPassword"
})
public class Member {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
<<<<<<< HEAD
  @Column(name = "MEMBER_ID", nullable = false, unique = true, length = 20)
=======
  @Column(name = "MEMBER_ID", nullable = false, unique = true)
>>>>>>> 7ff4725808bb3cf3c15b619956ee3e649b0ba42c
  private String memberId;
  
  @Column(name = "MEMBER_PASSWORD", length = 200)
  private String memberPassword;
  
  @Column(name = "MEMBER_NAME", length = 20)
  private String memberName;
  
  @Column(name = "MEMBER_GENDER", length = 1)
  private String memberGender;
  
  @Column(name = "MEMBER_EMAIL", length = 200)
  private String memberEmail;
  
  @Column(name = "MEMBER_BIRTH", length = 6)
  private String memberBirth;
  
  @Column(name = "MEMBER_ADDR", length = 50)
  private String memberAddr;
  
  @Column(name = "MEMBER_ZIP", length = 5)
  private String memberZip;
  
  @Column(name = "MEMBER_REG", updatable = false)
  private LocalDateTime memberReg;
  
  @Builder.Default
  @Column(name = "IS_ACTIVE", length = 1, nullable = false)
  private String isActive = "Y";
  
  @Builder.Default
  @Column(name = "ROLE", length = 20)
  private String role = "ROLE_USER";
  
  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @Builder.Default
  private List<MemberSocial> socialLinks = new ArrayList<>();
  
  @PrePersist
  public void setMemberRegTime() {
    if (this.memberReg == null) {
      this.memberReg = LocalDateTime.now();
    }
  }
}