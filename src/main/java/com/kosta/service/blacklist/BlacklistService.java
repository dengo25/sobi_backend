package com.kosta.service.blacklist;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kosta.domain.blacklist.Blacklist;
import com.kosta.repository.blacklist.BlacklistRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BlacklistService {
	
	private final BlacklistRepository blacklistRepository;
	
	public List<Blacklist> getBlockedMember(){
		return blacklistRepository.getBlockedMember();
	}
}
