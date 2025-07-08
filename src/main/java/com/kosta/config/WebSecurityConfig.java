package com.kosta.config;

import com.kosta.security.JwtAuthenticationFilter;
import com.kosta.security.OAuthSuccessHandler;
import com.kosta.security.RedirectUrlCookieFilter;
import com.kosta.security.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
  
  /*  final 키워드는 불편객체로 설정
    JwtAuthenticationFilter는 우리가 만든 jwt 기반 인증 필터이고, 이 필터를 스프링 시큐리티 필터체인에 추가하기 위해
    생성자 주입 방식으로 WebSecurityConfig에 전달하는 방식
  */
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  
  
  private final OAuthSuccessHandler oAuthSuccessHandler;
  
  //리다이렉트 url정보를 쿠키에 저장하는 필터
  private final RedirectUrlCookieFilter redirectUrlFilter;
  
  
  //필터체인에 등록하려면 JwtAuthenticationFilter에서 이 객체를 사용할 수 있어야하기 때문에 생성자 주입방식 사용
  public WebSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter
      , OAuthSuccessHandler oAuthSuccessHandler
      , RedirectUrlCookieFilter redirectUrlFilter) {
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    this.oAuthSuccessHandler = oAuthSuccessHandler;
    this.redirectUrlFilter = redirectUrlFilter;
  }
  
  //보안 필터 체인 정의
  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http, CustomOAuth2UserService customOAuth2UserService) throws Exception {
    http
        .cors(cors -> {}) //cors설정 활성화
        .csrf(csrf -> csrf.disable()) //csrf 비활성화 (restApi 서버에서 주로 사용)
        .httpBasic(httpBasic -> httpBasic.disable()) //기본 인증 방식 비 활성화
        //basic 인증은 id와 비밀번호를 매 요청마다 헤더에 실어서 보내는 방식
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션 사용 안 함 (JWT기반 인증)
        )
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/review/edit/**").authenticated()
            .requestMatchers("/api/admin/**").permitAll()
            .requestMatchers("/", "/auth/**","/api/review/**","/api/review/detail/**").permitAll() //루트 및 /auth/** 경로는 인증 없이 허용
            .requestMatchers("/api/mypage/**").authenticated() // 마이페이지 인증 필요
            .requestMatchers("/api/messages/**").authenticated() // 쪽지 기능 인증 필요
            .anyRequest().authenticated() //나머지 요청은 인증 필요
        )
        
        //만든 jwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 이후에 실행되도록 추가
        .addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        
        //OAuth2 로그인 설정
        .oauth2Login(oauth2 -> oauth2
            .userInfoEndpoint(userInfo ->userInfo
                .userService(customOAuth2UserService) //OAuth2 사용자 정보 로딩
            )
            .successHandler(oAuthSuccessHandler)
        )
        
        .exceptionHandling(exception -> exception
            //인증 실패 시 403 Forbidden 반환
            .authenticationEntryPoint(new Http403ForbiddenEntryPoint())
        )
        
        //리다이렉트 url 쿠키필터를 OAuth2 리다이렉트 필터 이전에 실행
        .addFilterBefore(redirectUrlFilter, OAuth2AuthorizationRequestRedirectFilter.class);
    
    return http.build();
  }
  
  // CORS 설정을 담당하는 메서드
  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowCredentials(true); //자격증명 포함 허용(ex: 쿠키, Authorication 헤더)
    configuration.setAllowedOrigins(List.of("http://localhost:5173")); //허용할 프론트엔드 도메인
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")); //허용 메서드
    configuration.setAllowedHeaders(List.of("*")); //모든 요청 헤더 허용
    configuration.setExposedHeaders(List.of("*")); //응답 헤더 노출
    
    
    // 위의 CORS 설정을 모든 경로에 적용
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration); //모든 요청에 대해 설정 적용
    return source;
  }
  
}            //.requestMatchers("/", "/auth/**","/api/review/**").permitAll() //루트 및 /auth/** 경로는 인증 없이 허용

