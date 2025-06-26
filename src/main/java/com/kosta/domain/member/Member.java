package com.kosta.domain.member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "MEMBER")
public class Member {
  @Id
  @Column(name = "MEMBER_ID",nullable = false)
  private String memberId;
  
  @Column(name = "MEMBER_PASSWORD",length = 30)
  private String memberPassword;
  
  @Column(name = "MEMBER_NAME",length = 20)
  private String memberName;
  
  @Column(name = "MEMBER_GENDER",length = 1)
  private String memberGender;
  
  @Column(name = "MEMBER_EMAIL",length = 200)
  private String memberEmail;
  
  @Column(name = "MEMBER_BIRTH",length = 10)
  private String memberBirth;
  
  @Column(name = "MEMBER_ADDR",length = 50)
  private String memberAddr;
  
  @Column(name = "MEMBER_REG",length = 10)
  private String memberZip;
  
  @Column(name = "IS_ACTIVE", length = 1)
  private String isActive = "Y";
  
  @Column(name = "ROLE", length = 1)
  private String role = "M";
}
