package com.kosta.service.blacklist;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.kosta.domain.blacklist.Blacklist;
import com.kosta.domain.blacklisthistory.BlacklistHistory;
import com.kosta.domain.member.Member;
import com.kosta.dto.blacklist.BlacklistRequestDto;
import com.kosta.repository.admin.AdminRepository;
import com.kosta.repository.blacklist.BlacklistRepository;
import com.kosta.repository.blacklisthistory.BlacklistHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BlacklistServiceImpl implements BlacklistService {

    private final BlacklistRepository blacklistRepository;
    private final BlacklistHistoryRepository blacklistHistoryRepository;
    private final AdminRepository adminRepository;

    @Override
    public List<Blacklist> getBlockedMember() {
        return blacklistRepository.getBlockedMember();
    }
    
    @Override
    public Long getBlockedCount() {
        return blacklistRepository.countByStatus("BLOCKED");
    }
    
    @Override
    public void removeFromBlacklist(int blacklistNo, String reason) {
    	Optional<Blacklist> blacklistOpt = blacklistRepository.findById(blacklistNo);
        if (blacklistOpt.isEmpty()) {
            throw new RuntimeException("해당 블랙리스트가 존재하지 않습니다.");
        }
        
        Blacklist blacklist = blacklistOpt.get();
        Member member = blacklist.getMember();
        
        
        // 상태를 UNBLOCKED로 변경
        blacklist.setStatus("UNBLOCKED");
        blacklist.setUpdateAt(LocalDateTime.now());
        blacklistRepository.save(blacklist);
        
        // 블랙리스트 해제 이력 추가
        BlacklistHistory history = BlacklistHistory.builder()
            .blacklist(blacklist)
            .detail("블랙리스트 해제: " + reason)
            .reportType("UNBLOCK")
            .build();
        
        blacklistHistoryRepository.save(history);
        
        // 회원 상태 활성화
        member.setIsActive("Y");
        adminRepository.save(member);
        
        log.info("블랙리스트 해제 완료 - 회원: {}, 사유: {}", member.getMemberId(), reason);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isBlacklisted(String memberId) {
        Member member = adminRepository.findByMemberId(memberId);
        if (member == null) {
            return false;
        }
        
        return blacklistRepository.findActiveBlacklistByMember(member).isPresent();
    }
}