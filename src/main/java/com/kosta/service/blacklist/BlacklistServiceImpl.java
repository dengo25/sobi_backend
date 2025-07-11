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
    public void addToBlacklist(BlacklistRequestDto requestDto) {
        // 대상 회원 조회
        Member member = adminRepository.findByMemberId(requestDto.getMemberId());
        if (member == null) {
            throw new RuntimeException("존재하지 않는 회원입니다: " + requestDto.getMemberId());
        }
        
        // 기존 블랙리스트 확인
        Optional<Blacklist> existingBlacklist = blacklistRepository.findByMember(member);
        
        Blacklist blacklist;
        if (existingBlacklist.isPresent()) {
            blacklist = existingBlacklist.get();
            // 이미 차단된 상태인지 확인
            if ("BLOCKED".equals(blacklist.getStatus())) {
                throw new RuntimeException("이미 블랙리스트에 등록된 회원입니다.");
            }
            // 상태를 BLOCKED로 변경
            blacklist.setStatus("BLOCKED");
            blacklist.setUpdateAt(LocalDateTime.now());
        } else {
            // 새로운 블랙리스트 생성
            blacklist = new Blacklist();
            blacklist.setMember(member);
            blacklist.setStatus("BLOCKED");
        }
        
        blacklistRepository.save(blacklist);
        
        // 블랙리스트 이력 추가
        BlacklistHistory history = BlacklistHistory.builder()
            .blacklist(blacklist)
            .detail(requestDto.getReason())
            .reportType(requestDto.getReportType())
            .build();
        
        blacklistHistoryRepository.save(history);
        
        // 회원 상태 비활성화
        member.setIsActive("N");
        adminRepository.save(member);
        
        log.info("블랙리스트 추가 완료 - 회원: {}, 사유: {}", 
                requestDto.getMemberId(), requestDto.getReason());
    }
    
    @Override
    public void removeFromBlacklist(String memberId, String reason) {
        Member member = adminRepository.findByMemberId(memberId);
        if (member == null) {
            throw new RuntimeException("존재하지 않는 회원입니다: " + memberId);
        }
        
        Optional<Blacklist> blacklistOpt = blacklistRepository.findActiveBlacklistByMember(member);
        if (blacklistOpt.isEmpty()) {
            throw new RuntimeException("블랙리스트에 등록되지 않은 회원입니다.");
        }
        
        Blacklist blacklist = blacklistOpt.get();
        
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
        
        log.info("블랙리스트 해제 완료 - 회원: {}, 사유: {}", memberId, reason);
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