package com.kosta.service.admin;

import org.springframework.stereotype.Service;

import com.kosta.repository.admin.AdminRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {
	private final AdminRepository adminRepository;
	
	public long getTodayMember() {
		return adminRepository.getTodayJoinMember();
	}
	
}
