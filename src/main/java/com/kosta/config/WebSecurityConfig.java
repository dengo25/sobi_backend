package com.kosta.config;

import com.kosta.security.JwtAuthenticationFilter;
import com.kosta.security.OAuthSuccessHandler;
import com.kosta.security.RedirectUrlCookieFilter;
import com.kosta.security.service.CustomOAuth2UserService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.ForwardedHeaderFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
  
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final OAuthSuccessHandler oAuthSuccessHandler;
  private final RedirectUrlCookieFilter redirectUrlFilter;
  
  public WebSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter
      , OAuthSuccessHandler oAuthSuccessHandler
      , RedirectUrlCookieFilter redirectUrlFilter) {
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    this.oAuthSuccessHandler = oAuthSuccessHandler;
    this.redirectUrlFilter = redirectUrlFilter;
  }
  
  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http, CustomOAuth2UserService customOAuth2UserService,
                                          ClientRegistrationRepository clientRegistrationRepository
  ) throws Exception {
    http
        .cors(cors -> {})
        .csrf(csrf -> csrf.disable())
        .httpBasic(httpBasic -> httpBasic.disable())
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
        )
        .authorizeHttpRequests(auth -> auth
            // OAuth2 관련 경로들을 먼저 허용
            .requestMatchers("/oauth2/**", "/login/**", "/login/oauth2/**", "/error").permitAll()
            
            // 기타 public 경로들
            .requestMatchers("/api/s3/presigned").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/faq").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/faq/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/notice").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/notice/**").permitAll()
            .requestMatchers("/", "/auth/**","/api/review/**").permitAll()
            .requestMatchers("/api/review/detail/**").permitAll()
            
            // 인증이 필요한 경로들
            .requestMatchers("/api/review/edit/**").authenticated()
            .requestMatchers("/api/review/my-reviews").authenticated()
            .requestMatchers("/api/mypage/**").authenticated()
            .requestMatchers("/api/messages/**").authenticated()
            .requestMatchers(HttpMethod.POST, "/api/faq").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/api/faq/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/faq/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.POST, "/api/notice").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/api/notice/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/notice/**").hasRole("ADMIN")
            .anyRequest().authenticated()
        )
        
        // OAuth2 로그인 설정 (authorizationRequestCustomizer 제거)
        .oauth2Login(oauth2 -> oauth2
            .userInfoEndpoint(userInfo -> userInfo
                .userService(customOAuth2UserService)
            )
            .successHandler(oAuthSuccessHandler)
            .failureHandler((request, response, exception) -> {
              System.out.println("=== OAuth2 로그인 실패 ===");
              System.out.println("오류: " + exception.getMessage());
              exception.printStackTrace();
              response.sendRedirect("/login?error=" + exception.getMessage());
            })
        )
        
        .exceptionHandling(exception -> exception
            .authenticationEntryPoint(new Http403ForbiddenEntryPoint())
        )
        
        // 필터 순서 조정: OAuth2 관련 필터들을 JWT 필터보다 먼저 실행
        .addFilterBefore(redirectUrlFilter, OAuth2AuthorizationRequestRedirectFilter.class)
        .addFilterAfter(jwtAuthenticationFilter, OAuth2AuthorizationRequestRedirectFilter.class);
    
    return http.build();
  }
  
  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowCredentials(true);
    configuration.setAllowedOrigins(List.of(
        "http://localhost:5173",
        "http://sobi-front.s3-website.ap-northeast-2.amazonaws.com",
        "https://sobi.thekosta.com"));
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
    configuration.setAllowedHeaders(List.of("*"));
    configuration.setExposedHeaders(List.of("*"));
    
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
  
  @Bean
  FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter() {
    FilterRegistrationBean<ForwardedHeaderFilter> bean = new FilterRegistrationBean<>();
    bean.setFilter(new ForwardedHeaderFilter());
    return bean;
  }
}