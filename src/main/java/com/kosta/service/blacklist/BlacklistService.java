package com.kosta.service.blacklist;

import java.util.List;
import com.kosta.domain.blacklist.Blacklist;

public interface BlacklistService {

    List<Blacklist> getBlockedMember();
    Long getBlockedCount();
    
    
    void removeFromBlacklist(int blacklistNo, String reason);
    boolean isBlacklisted(String memberId);
    
    
}