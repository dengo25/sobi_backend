package com.kosta.service.blacklist;

import java.util.List;
import com.kosta.domain.blacklist.Blacklist;
import com.kosta.dto.blacklist.BlacklistRequestDto;

public interface BlacklistService {

    List<Blacklist> getBlockedMember();
    Long getBlockedCount();
    
    
    void removeFromBlacklist(int blacklistNo, String reason);
    boolean isBlacklisted(String memberId);
    
    
}