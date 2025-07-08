package com.kosta.controller.blacklist;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kosta.domain.blacklist.Blacklist;
import com.kosta.service.blacklist.BlacklistService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class BlacklistController {

	private final BlacklistService blacklistService;
	
}
