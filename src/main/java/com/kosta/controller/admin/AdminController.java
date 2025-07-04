package com.kosta.controller.admin;

import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kosta.controller.member.MemberController;
import com.kosta.dto.member.MemberInfoDto;
import com.kosta.security.TokenProvider;
import com.kosta.security.vo.CustomUserDetails;
import com.kosta.service.member.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
	@GetMapping("/role")
	public ResponseEntity<?> getRole(@AuthenticationPrincipal CustomUserDetails userDetails) {
	    String memberId = userDetails.getUsername();
	    String role = userDetails.getRole();

	    return ResponseEntity.ok(new MemberInfoDto(memberId, role));
	}

}
