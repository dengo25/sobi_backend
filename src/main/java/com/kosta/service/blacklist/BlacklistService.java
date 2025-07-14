package com.kosta.service.blacklist;

import java.util.List;
import com.kosta.domain.blacklist.Blacklist;
import com.kosta.dto.blacklist.BlacklistRequestDto;

public interface BlacklistService {

    List<Blacklist> getBlockedMember();
    Long getBlockedCount();
    
    // 새로운 메서드들 추가
    void addToBlacklist(BlacklistRequestDto requestDto);
    void removeFromBlacklist(String memberId, String reason);
    boolean isBlacklisted(String memberId);
}