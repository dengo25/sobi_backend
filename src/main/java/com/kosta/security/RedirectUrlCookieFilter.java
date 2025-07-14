package com.kosta.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class RedirectUrlCookieFilter extends OncePerRequestFilter {
	
	public static final String REDIRECT_URI_PARAM = "redirect_url";
	private static final int MAX_AGE = 3000;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		if (request.getRequestURI().startsWith("/oauth2/authorization")) {
			try {
				log.info("=== [RedirectUrlCookieFilter] 요청 감지 ===");
				log.info("Request URI: {}", request.getRequestURI());
				log.info("Query string: {}", request.getQueryString());
				
				// 기존 쿠키 로깅
				Cookie[] cookies = request.getCookies();
				if (cookies != null) {
					for (Cookie c : cookies) {
						log.info("기존 쿠키: {} = {}", c.getName(), c.getValue());
					}
				}
				
				String redirectUrl = request.getParameter(REDIRECT_URI_PARAM);
				log.info("redirect_url param: {}", redirectUrl);
				
				if (redirectUrl == null || redirectUrl.isBlank()) {
					redirectUrl = "https://sobi.thekosta.com/review/list";
				}
				
				// ✅ 커스텀 검증 메서드 사용
				if (!isValidRedirectUrl(redirectUrl)) {
					log.warn("유효하지 않은 redirect_url: {}", redirectUrl);
					redirectUrl = "https://sobi.thekosta.com/dashboard";
				}
				
				log.info("최종 redirect_url: {}", redirectUrl);
				
				// ✅ 헤더로만 설정 (Secure, SameSite 포함)
				String cookieHeader = String.format(
						"%s=%s; Path=/; HttpOnly; Secure; Max-Age=%d; SameSite=Lax",
						REDIRECT_URI_PARAM, redirectUrl, MAX_AGE
				);
				response.addHeader("Set-Cookie", cookieHeader);
				log.info("쿠키 설정: {}", cookieHeader);
				
			} catch (Exception ex) {
				log.error("RedirectUrlCookieFilter 에러", ex);
			}
		}
		
		filterChain.doFilter(request, response);
	}
	
	// ✅ 커스텀 검증 메서드
	private boolean isValidRedirectUrl(String url) {
		if (url == null || url.isBlank()) {
			return false;
		}
		
		String[] allowedDomains = {
				"https://sobi.thekosta.com",
				"http://localhost:3000",
				"http://localhost:8080"
		};
		
		return java.util.Arrays.stream(allowedDomains)
				.anyMatch(domain -> url.startsWith(domain));
	}
}