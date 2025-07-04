package com.kosta.security.vo;

import com.kosta.security.dto.OAuthAttributes;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;


public class CustomMember extends DefaultOAuth2User{
  private static final long serialVersionUID = 1L;
  
  private Long id;
  private String memberId;
  private String memberName;
  private String memberEmail;
  private String role;
  
  public CustomMember(Long id,
                      String memberId,
                      String memberName,
                      String memberEmail,
                      String role,
                      Collection<? extends GrantedAuthority> authorities,
                      OAuthAttributes attributes) {
    super(authorities, attributes.getAttributes(), attributes.getNameAttributeKey());
    this.id = id;
    this.memberId = memberId;
    this.memberName = memberName;
    this.memberEmail = memberEmail;
    this.role = role;
  }
  
  public Long getId() {
    return id;
  }
  
  public String getMemberId() {
    return memberId;
  }
  
  public String getMemberName() {
    return memberName;
  }
  
  public String getMemberEmail() {
    return memberEmail;
  }
  
  public String getRole() {
    return role;
  }
  
  @Override
  public String getName() {
    return String.valueOf(id); // Security에서 사용하는 식별자
  }
}