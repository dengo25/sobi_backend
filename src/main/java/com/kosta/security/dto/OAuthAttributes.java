package com.kosta.security.dto;

import lombok.Getter;
import lombok.Builder;

import java.util.Map;

@Getter
public class OAuthAttributes {
  private Map<String, Object> attributes;
  private String nameAttributeKey;
  
  // 너의 Member 테이블에 맞춘 필드
  private String memberId;      // 소셜 고유 ID → memberId로 사용 가능
  private String memberName;    // 이름 or 닉네임
  private String memberEmail;   // 이메일
  private String profileImage;  // 프로필 사진 (선택)
  
  @Builder
  public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey,
                         String memberId, String memberName, String memberEmail, String profileImage) {
    this.attributes = attributes;
    this.nameAttributeKey = nameAttributeKey;
    this.memberId = memberId;
    this.memberName = memberName;
    this.memberEmail = memberEmail;
    this.profileImage = profileImage;
  }
  
  public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
    return switch (registrationId) {
//      case "kakao" -> ofKakao(userNameAttributeName, attributes);
//      case "naver" -> ofNaver(userNameAttributeName, attributes);
//      case "github" -> ofGitHub(userNameAttributeName, attributes);
      default -> ofGoogle(userNameAttributeName, attributes);
    };
  }
  
  private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
    return OAuthAttributes.builder()
        .memberId((String) attributes.get(userNameAttributeName)) // 보통 sub 또는 id
        .memberName((String) attributes.get("name"))
        .memberEmail((String) attributes.get("email"))
        .profileImage((String) attributes.get("picture"))
        .attributes(attributes)
        .nameAttributeKey(userNameAttributeName)
        .build();
  }



}