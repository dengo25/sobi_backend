package com.kosta.domain.member;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MEMBER")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Member {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(name = "MEMBER_ID",nullable = false,length = 20)
  private String memberId;
  
  @Column(name = "MEMBER_PASSWORD",length = 200)
  private String memberPassword;
  
  @Column(name = "MEMBER_NAME",length = 20)
  private String memberName;
  
  @Column(name = "MEMBER_GENDER",length = 1)
  private String memberGender;
  
  @Column(name = "MEMBER_EMAIL",length = 200)
  private String memberEmail;
  
  @Column(name = "MEMBER_BIRTH",length = 6)
  private String memberBirth;
  
  @Column(name = "MEMBER_ADDR",length = 50)
  private String memberAddr;
  
  @Column(name = "MEMBER_ZIP",length = 5)
  private String memberZip;
  
  @Builder.Default
  @Column(name = "IS_ACTIVE", length = 1,nullable = false)
  private String isActive = "Y";
  
  @Builder.Default
  @Column(name = "ROLE", length = 20)
  private String role = "ROLE_USER";
  
  @OneToMany(mappedBy = "member") //여기서 "member"는 MemberSocial의 필드이름
  private List<MemberSocial> socialLinks = new ArrayList<>();
  
  
}
