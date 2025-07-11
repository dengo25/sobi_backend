package com.kosta.repository.blacklisthistory;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.kosta.domain.blacklist.Blacklist;
import com.kosta.domain.blacklisthistory.BlacklistHistory;

@Repository
public interface BlacklistHistoryRepository extends JpaRepository<BlacklistHistory, Integer> {
    
    // 특정 블랙리스트의 이력 조회
    List<BlacklistHistory> findByBlacklistOrderByHistoryCreatedAtDesc(Blacklist blacklist);
    
    // 특정 회원의 블랙리스트 이력 조회
    @Query("SELECT bh FROM BlacklistHistory bh " +
           "JOIN bh.blacklist b " +
           "WHERE b.member.memberId = :memberId " +
           "ORDER BY bh.historyCreatedAt DESC")
    List<BlacklistHistory> findByMemberIdOrderByHistoryCreatedAtDesc(@Param("memberId") String memberId);
}