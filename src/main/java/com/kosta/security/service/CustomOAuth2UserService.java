package com.kosta.security.service;

import com.kosta.domain.member.Member;
import com.kosta.repository.member.MemberRepository;
import com.kosta.security.dto.OAuthAttributes;
import com.kosta.security.vo.CustomMember;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
  @Autowired
  private MemberRepository memberRepository; //사용자 정보를 처리할 UserRepository 의존성 주입
  
  
  //OAuth2 로그인 시 사용자의 정보를 가져와서 CustomUser 객체로 반환하는 메서드
  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    try {
      log.info("loadUser");
      
      OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
      OAuth2User oAuth2User = delegate.loadUser(userRequest);
      
      String registrationId = userRequest.getClientRegistration().getRegistrationId();
      String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
          .getUserInfoEndpoint().getUserNameAttributeName();
      
      log.info("loadUser registrationId = " + registrationId);
      log.info("loadUser userNameAttributeName = " + userNameAttributeName);
      
      OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
      
      String nameAttributeKey = attributes.getNameAttributeKey();
      String name = attributes.getMemberName();
      String email = attributes.getMemberEmail();
      String picture = attributes.getProfileImage();
      String id = attributes.getMemberId();
      String socialType = "";
      
      if ("github".equals(registrationId)) {
        socialType = "github";
        if (email == null) {
          log.info("loadUser userRequest.getAccessToken().getTokenValue() = " + userRequest.getAccessToken().getTokenValue());
          email = getEmailFromGitHub(userRequest.getAccessToken().getTokenValue());
          log.info("loadUser GitHub email = " + email);
        }
      } else {
        socialType = "google";
      }
      
      log.info("loadUser nameAttributeKey = " + nameAttributeKey);
      log.info("loadUser id = " + id);
      log.info("loadUser socialType = " + socialType);
      log.info("loadUser name = " + name);
      log.info("loadUser email = " + email);
      log.info("loadUser attributes = " + attributes);
      
      if (name == null) name = "";
      if (email == null) email = "";
      
      List<SimpleGrantedAuthority> authorities = new ArrayList<>();
      authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
      
      String username = email;
      String authProvider = socialType;
      Member member;
      
      if (!memberRepository.existsByMemberId(username)) {
        member = Member.builder()
            .memberId(username)
            .memberEmail(email)
            .build();
        member = memberRepository.save(member);
      } else {
        member = memberRepository.findByMemberId(username);
      }
      
      log.info("Successfully pulled user info username {} authProvider {}", username, authProvider);
      
      return new CustomMember(
          member.getId(),
          member.getMemberId(),
          member.getMemberName(),
          member.getMemberEmail(),
          member.getRole(),
          authorities,
          attributes
      );
      
    } catch (Exception e) {
      log.error("OAuth2 로그인 중 예외 발생: {}", e.getMessage(), e);
      
      OAuth2Error error = new OAuth2Error("oauth2_authentication_failed", e.getMessage(), null);
      throw new OAuth2AuthenticationException(error, e.getMessage(), e);
    }
  }
  
  private String getEmailFromGitHub(String accessToken) {
    String url = "https://api.github.com/user/emails"; //github 이메일 API

    RestTemplate restTemplate = new RestTemplate(); //HTTP 요청 도구

    //헤더 추가
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + accessToken);
    headers.set("Accept", "application/vnd.github.v3+json");

    HttpEntity<String> entity = new HttpEntity<>(headers);

    ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);

    List<Map<String, Object>> emails = response.getBody();

    if (emails != null) {
      for (Map<String, Object> emailData : emails) {
        if ((Boolean) emailData.get("primary")) {
          return (String) emailData.get("email");
        }
      }
    }

    return null;
  }
  
}