package com.kosta.config;

import java.util.List;

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

import com.kosta.security.JwtAuthenticationFilter;
import com.kosta.security.OAuthSuccessHandler;
import com.kosta.security.RedirectUrlCookieFilter;
import com.kosta.security.service.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final OAuthSuccessHandler oAuthSuccessHandler;
	private final RedirectUrlCookieFilter redirectUrlFilter;

	public WebSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, OAuthSuccessHandler oAuthSuccessHandler,
			RedirectUrlCookieFilter redirectUrlFilter) {
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
		this.oAuthSuccessHandler = oAuthSuccessHandler;
		this.redirectUrlFilter = redirectUrlFilter;
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http, CustomOAuth2UserService customOAuth2UserService)
			throws Exception {
		http.cors(cors -> {
		}).csrf(csrf -> csrf.disable()).httpBasic(httpBasic -> httpBasic.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(
						auth -> auth.requestMatchers("/", "/auth/login", "/auth/signup", "/review", "/review/detail/**")
								.permitAll().requestMatchers("/auth/mypage").authenticated()
								.requestMatchers("/review/**").authenticated().anyRequest().authenticated())

				.addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

				.oauth2Login(
						oauth2 -> oauth2.userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
								.successHandler(oAuthSuccessHandler))

				.exceptionHandling(exception -> exception.authenticationEntryPoint(new Http403ForbiddenEntryPoint()))

				.addFilterBefore(redirectUrlFilter, OAuth2AuthorizationRequestRedirectFilter.class);

		return http.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowCredentials(true);
		configuration.setAllowedOrigins(List.of("http://localhost:5173"));
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setExposedHeaders(List.of("*"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}